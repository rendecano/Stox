package io.rendecano.stox.list.data.local.source.impl

import androidx.lifecycle.LiveData
import io.rendecano.stox.common.data.local.AppDatabase
import io.rendecano.stox.list.data.local.model.PriceHistoryEntity
import io.rendecano.stox.list.data.local.model.StockEntity
import io.rendecano.stox.list.data.local.source.StockLocalSource
import javax.inject.Inject

class StockLocalDataSource @Inject constructor(private val database: AppDatabase) :
        StockLocalSource {

    override suspend fun saveStockList(stockEntityList: List<StockEntity>) =
            database.stockDao().insertAll(stockEntityList)

    override suspend fun retrieveStockList(): List<StockEntity> =
            database.stockDao().getStocks()

    override suspend fun getStock(symbol: String): StockEntity =
            database.stockDao().getStock(symbol)

    override suspend fun updateStock(stockEntity: StockEntity) =
            database.stockDao().updateStock(stockEntity)

    override fun retrieveFavoriteStockList(): LiveData<List<StockEntity>> =
            database.stockDao().getFavoriteStocks()

    override fun retrieveStocksList(): LiveData<List<StockEntity>> =
            database.stockDao().getStocksList()

    override fun retrieveFavoritesStockList(): List<StockEntity> =
            database.stockDao().getFavoriteStocksList()

    override fun retrieveStock(symbol: String): LiveData<StockEntity> =
            database.stockDao().getStockInfo(symbol)

    override suspend fun savePriceHistoryList(priceHistoryEntity: List<PriceHistoryEntity>) =
            database.priceHistoryDao().insertAll(priceHistoryEntity)

    override fun retrievePriceHistory(symbol: String): LiveData<List<PriceHistoryEntity>> =
            database.priceHistoryDao().getHistoricalPrice(symbol)

}