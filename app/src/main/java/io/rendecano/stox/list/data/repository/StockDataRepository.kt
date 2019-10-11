package io.rendecano.stox.list.data.repository

import io.rendecano.stox.list.data.local.model.StockEntity
import io.rendecano.stox.list.data.local.source.StockLocalSource
import io.rendecano.stox.list.data.remote.source.StockRemoteSource
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.domain.repository.StockRepository
import javax.inject.Inject

class StockDataRepository @Inject constructor(
        private val stockLocalSource: StockLocalSource,
        private val stockRemoteSource: StockRemoteSource
) : StockRepository {
    override suspend fun getCompanyProfiles(symbols: String): List<Stock> =
            stockRemoteSource.getCompanyProfiles(symbols)
                    .map {
                        Stock(it.profile.companyName,
                                it.profile.price,
                                it.symbol,
                                it.profile.image,
                                it.profile.lastDiv,
                                it.profile.changes,
                                it.profile.changesPercentage,
                                it.profile.industry,
                                it.profile.sector)
                    }


    override suspend fun getStockList(): List<Stock> =
            stockRemoteSource.getStockList()
                    .map {
                        Stock(it.name,
                                it.price,
                                it.symbol)
                    }


    override suspend fun saveStockList(stockList: List<Stock>) =
            stockLocalSource.saveStockList(stockList
                    .map {
                        StockEntity(it.symbol,
                                it.name,
                                it.price,
                                it.imageUrl,
                                it.lastDividend,
                                it.priceChange,
                                it.changesPercentage,
                                it.industry,
                                it.sector,
                                it.isFavorite)
                    })


}