package io.rendecano.stox.list.data.remote.model

data class HistoricalPriceList(
    val historical: List<Historical>,
    val symbol: String
)