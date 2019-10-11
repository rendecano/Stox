package io.rendecano.stox.list.domain.repository

import io.rendecano.stox.list.domain.model.Stock

interface StockRepository {

    suspend fun getStockList(): List<Stock>

    suspend fun saveStockList(stockList: List<Stock>)

    suspend fun getCompanyProfiles(symbols: String): List<Stock>


}