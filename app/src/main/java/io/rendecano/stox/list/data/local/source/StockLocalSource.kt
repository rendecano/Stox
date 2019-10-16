package io.rendecano.stox.list.data.local.source

import androidx.lifecycle.LiveData
import io.rendecano.stox.list.data.local.model.PriceHistoryEntity
import io.rendecano.stox.list.data.local.model.StockEntity

interface StockLocalSource {

    suspend fun saveStockList(stockEntityList: List<StockEntity>)

    suspend fun retrieveStockList(): List<StockEntity>

    suspend fun getStock(symbol: String): StockEntity

    suspend fun updateStock(stockEntity: StockEntity)

    fun retrieveFavoriteStockList(): LiveData<List<StockEntity>>

    fun retrieveStocksList(): LiveData<List<StockEntity>>

    fun retrieveFavoritesStockList(): List<StockEntity>

    fun retrieveStock(symbol: String): LiveData<StockEntity>

    suspend fun savePriceHistoryList(priceHistoryEntity: List<PriceHistoryEntity>)

    fun retrievePriceHistory(symbol: String): LiveData<List<PriceHistoryEntity>>
}