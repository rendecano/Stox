package io.rendecano.stox.list.data.local.source.impl

import io.rendecano.stox.common.data.local.AppDatabase
import io.rendecano.stox.list.data.local.model.StockEntity
import io.rendecano.stox.list.data.local.source.StockLocalSource
import javax.inject.Inject

class StockLocalDataSource @Inject constructor(private val database: AppDatabase) :
        StockLocalSource {

    override suspend fun saveStockList(stockEntityList: List<StockEntity>) =
            database.stockDao().insertAll(stockEntityList)

    override suspend fun retrieveStockList(): List<StockEntity> = database.stockDao().getStocks()
}