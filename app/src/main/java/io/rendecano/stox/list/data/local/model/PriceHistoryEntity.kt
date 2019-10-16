package io.rendecano.stox.list.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PriceHistoryEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0L,
        var symbol: String = "",
        var price: Double = 0.0,
        var label: String = "",
        var date: String = ""
)