package io.rendecano.stox.list.data.local.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import io.rendecano.stox.list.data.local.model.StockEntity

interface StockLocalSource {

    suspend fun saveStockList(stockEntityList: List<StockEntity>)

    suspend fun retrieveStockList(): List<StockEntity>

    suspend fun getStock(symbol: String): StockEntity

    suspend fun updateStock(stockEntity: StockEntity)

    fun retrieveFavoriteStockList(): LiveData<List<StockEntity>>

    fun retrieveStocksList(): LiveData<List<StockEntity>>

    fun retrieveFavoritesStockList(): List<StockEntity>
}