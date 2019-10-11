package io.rendecano.stox.list.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StockEntity(
    @PrimaryKey var symbol: String = "",
    var name: String = "",
    var price: Double = 0.0,
    var imageUrl: String = "",
    var lastDividend: String = "",
    var priceChange: Double = 0.0,
    var changesPercentage: String = "",
    var industry: String = "",
    var sector: String = "",
    var isFavorite: Boolean = false
)