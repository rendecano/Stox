package io.rendecano.stox.list.domain.repository

import androidx.lifecycle.LiveData
import io.rendecano.stox.list.domain.model.Stock

interface StockRepository {

    suspend fun saveStockList(stockList: List<Stock>)

    suspend fun getCompanyProfile(symbol: String, isForcedUpdate: Boolean = false): Stock

    suspend fun updateStock(stock: Stock)

    fun getFavoriteStockList(): LiveData<List<Stock>>

    fun getStocksList(): LiveData<List<Stock>>

    suspend fun downloadStockList(): List<Stock>

    suspend fun getFavoriteStocksList(): List<Stock>
}