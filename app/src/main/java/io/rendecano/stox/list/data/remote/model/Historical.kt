package io.rendecano.stox.list.data.remote.model

data class Historical(
    val change: Double,
    val changeOverTime: Double,
    val changePercent: Double,
    val close: Double,
    val date: String,
    val high: Double,
    val label: String,
    val low: Double,
    val unadjustedVolume: Double,
    val volume: Double,
    val vwap: Double
)