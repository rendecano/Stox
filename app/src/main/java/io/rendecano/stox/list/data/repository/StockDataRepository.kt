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

    override suspend fun getCompanyProfile(symbol: String): Stock {

        val cachedStock = stockLocalSource.getStock(symbol)

        if (cachedStock.imageUrl.isEmpty()) {

            // Fetch from network
            val result = stockRemoteSource.getCompanyProfile(symbol)

            // save to local storage
            stockLocalSource.updateStock(StockEntity(result.symbol,
                    result.profile.companyName,
                    result.profile.price,
                    result.profile.image,
                    result.profile.lastDiv,
                    result.profile.changes,
                    result.profile.changesPercentage,
                    result.profile.industry,
                    result.profile.sector))
        }
        val it = stockLocalSource.getStock(symbol)
        return Stock(it.name,
                it.price,
                it.symbol,
                it.imageUrl,
                it.lastDividend,
                it.priceChange,
                it.changesPercentage,
                it.industry,
                it.sector)


//        return stockLocalSource.retrieveStockList().map {
//            Stock(it.name,
//                    it.price,
//                    it.symbol,
//                    it.imageUrl,
//                    it.lastDividend,
//                    it.priceChange,
//                    it.changesPercentage,
//                    it.industry,
//                    it.sector)
//        }
    }


    override suspend fun getStockList(): List<Stock> {

        // Implement cache timeout
        val cachedData = stockLocalSource.retrieveStockList()
        if (cachedData.isEmpty()) {
            val remoteList = stockRemoteSource.getStockList().take(1000)
                    .map {
                        Stock(it.name,
                                it.price,
                                it.symbol)
                    }

            stockLocalSource.saveStockList(remoteList
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

        return stockLocalSource.retrieveStockList().map {
            Stock(it.name,
                    it.price,
                    it.symbol,
                    it.imageUrl,
                    it.lastDividend,
                    it.priceChange,
                    it.changesPercentage,
                    it.industry,
                    it.sector)
        }
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

    override suspend fun updateStock(stock: Stock) {
        stockLocalSource.updateStock(StockEntity(stock.symbol,
                stock.name,
                stock.price,
                stock.imageUrl,
                stock.lastDividend,
                stock.priceChange,
                stock.changesPercentage,
                stock.industry,
                stock.sector,
                stock.isFavorite))
    }
}