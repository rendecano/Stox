package io.rendecano.stox.list.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.rendecano.stox.list.domain.model.Stock

interface StockRepository {

    suspend fun getStockList(): List<Stock>

    suspend fun saveStockList(stockList: List<Stock>)

    suspend fun getCompanyProfile(symbol: String): Stock

    suspend fun updateStock(stock: Stock)
}