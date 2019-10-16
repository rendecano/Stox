package io.rendecano.stox.list.data.remote.source

import io.rendecano.stox.list.data.remote.model.CompanyProfile
import io.rendecano.stox.list.data.remote.model.Historical
import io.rendecano.stox.list.data.remote.model.Symbols

interface StockRemoteSource {
    suspend fun getStockList(): List<Symbols>

    suspend fun getCompanyProfile(symbol: String): CompanyProfile

    suspend fun getRealtimePrice(symbol: String): Symbols

    suspend fun getHistoricalPriceList(symbol: String, fromDate: String, toDate: String): List<Historical>
}