package io.rendecano.stox.list.data.local.source

import androidx.paging.PagedList
import io.rendecano.stox.list.data.local.model.StockEntity

interface StockLocalSource {

    suspend fun saveStockList(stockEntityList: List<StockEntity>)

    suspend fun retrieveStockList(): List<StockEntity>

    suspend fun getStock(symbol: String): StockEntity

    suspend fun updateStock(stockEntity: StockEntity)

    suspend fun retrieveFavoriteStockList(): List<StockEntity>
}