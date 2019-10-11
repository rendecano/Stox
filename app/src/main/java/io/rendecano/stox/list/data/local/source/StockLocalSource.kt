package io.rendecano.stox.list.data.local.source

import io.rendecano.stox.list.data.local.model.StockEntity

interface StockLocalSource {

    suspend fun saveStockList(stockEntityList: List<StockEntity>)

    suspend fun retrieveStockList(): List<StockEntity>

}