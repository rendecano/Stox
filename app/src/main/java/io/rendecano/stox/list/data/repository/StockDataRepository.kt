package io.rendecano.stox.list.data.repository

import io.rendecano.stox.list.data.local.model.StockEntity
import io.rendecano.stox.list.data.local.source.StockLocalSource
import io.rendecano.stox.list.data.remote.model.CompanyProfile
import io.rendecano.stox.list.data.remote.source.StockRemoteSource
import io.rendecano.stox.list.domain.model.Status
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.domain.repository.StockRepository
import javax.inject.Inject

class StockDataRepository @Inject constructor(
        private val stockLocalSource: StockLocalSource,
        private val stockRemoteSource: StockRemoteSource
) : StockRepository {

    override suspend fun getCompanyProfile(symbol: String): Stock {

        val cachedStock = stockLocalSource.getStock(symbol)

        if (cachedStock.imageUrl.isEmpty()) {

            // Fetch from network
            val result = stockRemoteSource.getCompanyProfile(symbol)

            // save to local storage
            stockLocalSource.updateStock(result.toEntity())
        }
        val it = stockLocalSource.getStock(symbol)
        return it.toStock()
    }

    override suspend fun getStockList(): List<Stock> {

        //TODO: Implement cache timeout
        val cachedData = stockLocalSource.retrieveStockList()
        if (cachedData.isEmpty()) {
            val remoteList = stockRemoteSource.getStockList().take(50)
                    .map {
                        Stock(it.name,
                                it.price,
                                it.symbol)
                    }

            stockLocalSource.saveStockList(remoteList
                    .map { it.toEntity() })
        }

        return stockLocalSource.retrieveStockList()
                .map { it.toStock() }
    }

    override suspend fun saveStockList(stockList: List<Stock>) =
            stockLocalSource.saveStockList(stockList
                    .map { it.toEntity() })

    override suspend fun updateStock(stock: Stock) =
            stockLocalSource.updateStock(stock.toEntity())

    override suspend fun getFavoriteStockList(): List<Stock> = stockLocalSource.retrieveFavoriteStockList()
            .map { it.toStock() }

    private fun StockEntity.toStock(): Stock =
            Stock(this.name,
                    this.price,
                    this.symbol,
                    this.imageUrl,
                    this.lastDividend,
                    this.priceChange,
                    this.changesPercentage,
                    this.industry,
                    this.sector,
                    when (this.status) {
                        1 -> Status.GAIN
                        2 -> Status.LOSS
                        else -> Status.NO_CHANGE
                    },
                    this.isFavorite)

    private fun Stock.toEntity(): StockEntity =
            StockEntity(this.symbol,
                    this.name,
                    this.price,
                    this.imageUrl,
                    this.lastDividend,
                    this.priceChange,
                    this.changesPercentage,
                    this.industry,
                    this.sector,
                    this.isFavorite,
                    when (this.status) {
                        Status.GAIN -> 1
                        Status.LOSS -> 2
                        else -> 0
                    })

    private fun CompanyProfile.toEntity(): StockEntity =
            StockEntity(this.symbol,
                    this.profile.companyName,
                    this.profile.price,
                    this.profile.image,
                    this.profile.lastDiv,
                    this.profile.changes,
                    this.profile.changesPercentage,
                    this.profile.industry,
                    this.profile.sector,
                    false,
                    0)

}