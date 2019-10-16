package io.rendecano.stox.list.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.rendecano.stox.R
import io.rendecano.stox.common.presentation.ui.AsyncDiffUtil
import io.rendecano.stox.databinding.ListItemStockBinding
import io.rendecano.stox.list.domain.model.Stock

class StockListAdapter(private val bindingLifecycleOwner: LifecycleOwner,
                       private val favoriteListener: OnFavoriteListener,
                       private val selectListener: OnSelectListener) :
        RecyclerView.Adapter<StockListAdapter.StockViewHolder>() {

    interface OnFavoriteListener {
        fun onSelect(symbol: String, selected: Boolean)
    }

    interface OnSelectListener {
        fun onSelect(symbol: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return StockViewHolder(ListItemStockBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        holder.binding.apply {
            lifecycleOwner = bindingLifecycleOwner
            stock = asyncDiffUtil.current()[position]
            executePendingBindings()
        }

        holder.binding.buttonFavorite.setOnCheckedChangeListener { view, isChecked ->
            if (view.isPressed) {
                favoriteListener.onSelect(asyncDiffUtil.current()[position].symbol, isChecked)
            }
        }

        holder.binding.layoutInfo.setOnClickListener {
            selectListener.onSelect(asyncDiffUtil.current()[position].symbol)
        }

        holder.binding.txtPercentChange.setOnClickListener {
            selectListener.onSelect(asyncDiffUtil.current()[position].symbol)
        }
    }

    override fun getItemCount(): Int {
        return asyncDiffUtil.current().size
    }

    class StockViewHolder(val binding: ListItemStockBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return R.layout.list_item_stock
    }

    fun setData(newData: ArrayList<Stock>) = asyncDiffUtil.update(newData)

    private val asyncDiffUtil: AsyncDiffUtil<Stock> = AsyncDiffUtil(this, object : DiffUtil.ItemCallback<Stock>() {
        override fun areItemsTheSame(oldItem: Stock, newItem: Stock) = oldItem.symbol == newItem.symbol

        override fun areContentsTheSame(oldItem: Stock, newItem: Stock): Boolean = oldItem.price == newItem.price
    })
}