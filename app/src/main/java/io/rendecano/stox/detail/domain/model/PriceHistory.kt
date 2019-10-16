package io.rendecano.stox.detail.domain.model

enum class PriceRangeFilter {
    QUARTER, SEMI_ANNUAL, ANNUAL, THREE_YEARS
}

data class PriceHistory(val label: String = "",
                        val date: String = "",
                        var price: Double = 0.0)