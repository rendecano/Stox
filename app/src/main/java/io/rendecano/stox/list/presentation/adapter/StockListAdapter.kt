package io.rendecano.stox.list.presentation.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory
import io.rendecano.stox.R
import io.rendecano.stox.list.domain.model.Stock

class StockListAdapter(private val context: Context, private val itemList: List<Stock>) :
        RecyclerView.Adapter<StockListAdapter.StockViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_stock, parent, false)
        return StockViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val item = itemList[position]
        holder.txtName.text = item.name
        holder.txtSymbol.text = item.symbol
        holder.txtPrice.text = item.price.toString()
        holder.txtPercentChange.text = item.changesPercentage

        Glide.with(context).load(item.imageUrl)
                .dontAnimate()
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(holder.imgCompany)

        holder.imgFavorite.setOnClickListener {

        }

        holder.itemView.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class StockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtSymbol: TextView = view.findViewById(R.id.txtSymbol)
        var txtName: TextView = view.findViewById(R.id.txtName)
        var txtPrice: TextView = view.findViewById(R.id.txtPrice)
        var txtPercentChange: TextView = view.findViewById(R.id.txtPercentChange)
        var imgFavorite: ImageView = view.findViewById(R.id.imgFavorite)
        var imgCompany: ImageView = view.findViewById(R.id.imgCompany)
    }

    internal class DrawableAlwaysCrossFadeFactory : TransitionFactory<Drawable> {
        private val resourceTransition: DrawableCrossFadeTransition =
                DrawableCrossFadeTransition(300, true)

        override fun build(
                dataSource: DataSource?,
                isFirstResource: Boolean
        ): Transition<Drawable> {
            return resourceTransition
        }
    }
}