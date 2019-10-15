package io.rendecano.stox.common.presentation.ui


import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import java.util.*

class AsyncDiffUtil<T>(
        private val itemCallback: DiffUtil.ItemCallback<T>,
        private val listUpdateCallback: ListUpdateCallback,
        job: Job = Job()
) {

    internal sealed class UpdateListOperation {
        object Clear : UpdateListOperation()

        data class Insert(val newList: List<*>) : UpdateListOperation()
    }

    internal class SimpleUpdateCallback(private val adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) :
            ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyItemRangeRemoved(position, count)
        }
    }

    constructor(
            adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
            itemCallback: DiffUtil.ItemCallback<T>,
            job: Job = Job()
    ) : this(
            itemCallback, SimpleUpdateCallback(adapter), job
    )

    @Suppress("UNCHECKED_CAST")
    private val updateActor =
            GlobalScope.actor<UpdateListOperation>(
                    Dispatchers.Main,
                    Channel.UNLIMITED,
                    CoroutineStart.DEFAULT,
                    null, {

                consumeEach {
                    if (!isActive) return@actor

                    val oldList = list

                    when (it) {
                        UpdateListOperation.Clear -> {
                            if (oldList != null) {
                                clear(oldList.size)
                            }
                        }
                        is UpdateListOperation.Insert -> {
                            if (oldList == null) {
                                insert(it.newList)
                            } else if (oldList != it.newList) {
                                val callback =
                                        diffUtilCallback(oldList, it.newList as List<T>, itemCallback)
                                update(it.newList, callback)
                            }
                        }
                    }
                }
            })

    private var list: List<T>? = null
    private var readOnlyList: List<T> = emptyList()

    fun current() = readOnlyList

    fun update(newList: List<T>?) {
        if (newList == null) {
            updateActor.offer(UpdateListOperation.Clear)
        } else {
            updateActor.offer(UpdateListOperation.Insert(newList))
        }
    }

    private suspend fun clear(count: Int) {
        withContext(Dispatchers.Main) {
            list = null
            readOnlyList = emptyList()
            listUpdateCallback.onRemoved(0, count)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun insert(newList: List<*>) {
        withContext(Dispatchers.Main) {
            list = newList as List<T>
            readOnlyList = Collections.unmodifiableList(newList)
            listUpdateCallback.onInserted(0, newList.size)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun update(newList: List<*>, callback: DiffUtil.Callback) {
        withContext(Dispatchers.IO) {
            val result = DiffUtil.calculateDiff(callback)
            if (!coroutineContext.isActive) return@withContext
            latch(newList as List<T>, result)
        }
    }

    private suspend fun latch(newList: List<T>, result: DiffUtil.DiffResult) {
        withContext(Dispatchers.Main) {
            list = newList
            readOnlyList = Collections.unmodifiableList(newList)
            result.dispatchUpdatesTo(listUpdateCallback)
        }
    }

    private fun diffUtilCallback(oldList: List<T>, newList: List<T>, callback: DiffUtil.ItemCallback<T>) =
            object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = oldList[oldItemPosition]
                    val newItem = newList[newItemPosition]
                    return if (oldItem != null && newItem != null) {
                        callback.areItemsTheSame(oldItem, newItem)
                    } else {
                        oldItem == null && newItem == null
                    }
                }

                override fun getOldListSize(): Int = oldList.size

                override fun getNewListSize(): Int = newList.size

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = oldList[oldItemPosition]
                    val newItem = newList[newItemPosition]
                    return if (oldItem != null && newItem != null) {
                        callback.areContentsTheSame(oldItem, newItem)
                    } else if (oldItem == null && newItem == null) {
                        return true
                    } else {
                        throw AssertionError()
                    }
                }
            }
}