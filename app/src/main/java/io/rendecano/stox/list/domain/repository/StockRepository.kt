package io.rendecano.stox.list.domain.repository

import androidx.lifecycle.LiveData
import io.rendecano.stox.detail.domain.model.PriceHistory
import io.rendecano.stox.list.domain.model.Stock

interface StockRepository {

    suspend fun saveStockList(stockList: List<Stock>)

    suspend fun getCompanyProfile(symbol: String, isForcedUpdate: Boolean = false): Stock

    suspend fun updateStock(stock: Stock)

    suspend fun downloadStockList(): List<Stock>

    suspend fun getFavoriteStocksList(): List<Stock>

    suspend fun getRealtimePrice(symbol: String): Stock

    suspend fun getHistoricalPrice(symbol: String, fromDate: String, toDate: String): List<PriceHistory>

    suspend fun saveHistoricalPriceList(symbol: String, priceHistoryList: List<PriceHistory>)

    fun getFavoriteStockList(): LiveData<List<Stock>>

    fun getStocksList(): LiveData<List<Stock>>

    fun getStockDetails(symbol: String): LiveData<Stock>

    fun getPriceHistory(symbol: String): LiveData<List<PriceHistory>>


}