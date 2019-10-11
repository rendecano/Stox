package io.rendecano.stox.list.domain.model

enum class Status {
    GAIN, LOSS, NO_CHANGE
}

data class Stock(val name: String = "",
                 val price: Double = 0.0,
                 val symbol: String = "",
                 val imageUrl: String = "",
                 val lastDividend: String = "",
                 val priceChange: Double = 0.0,
                 val changesPercentage: String = "",
                 val industry: String = "",
                 val sector: String = "",
                 var status: Status = Status.NO_CHANGE,
                 val isFavorite: Boolean = false)