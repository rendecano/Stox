package io.rendecano.stox.list.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.rendecano.stox.R
import io.rendecano.stox.databinding.ListItemStockBinding
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.presentation.viewmodel.StocksListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class StockListAdapter(private val lifecycleOwne: LifecycleOwner, private val viewModels: StocksListViewModel) :
        RecyclerView.Adapter<StockListAdapter.StockViewHolder>() {

    private var itemList = ArrayList<Stock>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return StockViewHolder(ListItemStockBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {

        holder.binding.apply {
            viewModel = viewModels
            lifecycleOwner = lifecycleOwne
            stock = itemList[position]
            executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class StockViewHolder(val binding: ListItemStockBinding) : RecyclerView.ViewHolder(binding.root) {
    }

//    internal class DrawableAlwaysCrossFadeFactory : TransitionFactory<Drawable> {
//        private val resourceTransition: DrawableCrossFadeTransition =
//                DrawableCrossFadeTransition(300, true)
//
//        override fun build(
//                dataSource: DataSource?,
//                isFirstResource: Boolean
//        ): Transition<Drawable> {
//            return resourceTransition
//        }
//    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.list_item_stock
    }

    fun setData(newData: ArrayList<Stock>) {
        val diffCallback = StockDiffCallback(itemList, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        itemList.clear()
        itemList.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    class StockDiffCallback(private val oldList: List<Stock>, private val newList: List<Stock>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].symbol === newList[newItemPosition].symbol
        }

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
            return oldList[oldPosition].imageUrl == newList[newPosition].imageUrl
        }

        @Nullable
        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            // Implement method if you're going to use ItemAnimator
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}