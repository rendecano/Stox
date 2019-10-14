package io.rendecano.stox.list.data.remote.source

import io.rendecano.stox.list.data.remote.model.CompanyProfile
import io.rendecano.stox.list.data.remote.model.Symbols

interface StockRemoteSource {
    suspend fun getStockList(): List<Symbols>
    suspend fun getCompanyProfile(symbol: String): CompanyProfile
}