package io.rendecano.stox.list.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.rendecano.stox.detail.domain.model.PriceHistory
import io.rendecano.stox.list.data.local.model.PriceHistoryEntity
import io.rendecano.stox.list.data.local.model.StockEntity
import io.rendecano.stox.list.data.local.source.StockLocalSource
import io.rendecano.stox.list.data.remote.model.CompanyProfile
import io.rendecano.stox.list.data.remote.model.Historical
import io.rendecano.stox.list.data.remote.model.Symbols
import io.rendecano.stox.list.data.remote.source.StockRemoteSource
import io.rendecano.stox.list.domain.model.Status
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.domain.repository.StockRepository
import javax.inject.Inject

class StockDataRepository @Inject constructor(
        private val stockLocalSource: StockLocalSource,
        private val stockRemoteSource: StockRemoteSource
) : StockRepository {

    override suspend fun getCompanyProfile(symbol: String, isForcedUpdate: Boolean): Stock {

        //TODO: Implement cache timeout
        val cachedStock = stockLocalSource.getStock(symbol)
        return if (cachedStock.imageUrl.isEmpty() or isForcedUpdate) {
            stockRemoteSource.getCompanyProfile(symbol).toEntity().toStock()
        } else {
            cachedStock.toStock()
        }
    }

    override suspend fun saveStockList(stockList: List<Stock>) =
            stockLocalSource.saveStockList(stockList
                    .map { it.toEntity() })

    override suspend fun updateStock(stock: Stock) =
            stockLocalSource.updateStock(stock.toEntity())

    override fun getFavoriteStockList(): LiveData<List<Stock>> =
            Transformations.map(stockLocalSource.retrieveFavoriteStockList(), ::transform)

    override fun getStocksList(): LiveData<List<Stock>> =
            Transformations.map(stockLocalSource.retrieveStocksList(), ::transform)


    override suspend fun downloadStockList(): List<Stock> {

        //TODO: Implement cache timeout
        val cachedData = stockLocalSource.retrieveStockList()
        return if (cachedData.isEmpty()) {
            stockRemoteSource.getStockList().take(20)
                    .map { it.toStock() }
        } else {
            cachedData.map { it.toStock() }
        }
    }

    override suspend fun getFavoriteStocksList(): List<Stock> =
            stockLocalSource.retrieveFavoritesStockList().map { it.toStock() }

    override suspend fun getRealtimePrice(symbol: String): Stock =
            stockRemoteSource.getRealtimePrice(symbol).toStock()

    override suspend fun getHistoricalPrice(symbol: String, fromDate: String, toDate: String): List<PriceHistory> =
            stockRemoteSource.getHistoricalPriceList(symbol, fromDate, toDate).map {
                it.toPriceHistory()
            }

    override suspend fun saveHistoricalPriceList(symbol: String, priceHistoryList: List<PriceHistory>) =
            stockLocalSource.savePriceHistoryList(priceHistoryList.map { it.toPriceHistoryEntity(symbol) })

    override fun getStockDetails(symbol: String): LiveData<Stock> =
            Transformations.map(stockLocalSource.retrieveStock(symbol), ::transform)

    override fun getPriceHistory(symbol: String): LiveData<List<PriceHistory>> =
            Transformations.map(stockLocalSource.retrievePriceHistory(symbol), ::transformPriceEntity)

    private fun transform(listEntity: List<StockEntity>) = listEntity.map { it.toStock() }

    private fun transform(listEntity: StockEntity) = listEntity.toStock()

    private fun transformPriceEntity(priceHistoryEntityList: List<PriceHistoryEntity>) = priceHistoryEntityList.map { it.toPriceHistory() }

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

    private fun Symbols.toStock(): Stock =
            Stock(this.name ?: "",
                    this.price,
                    this.symbol)

    private fun PriceHistoryEntity.toPriceHistory() =
            PriceHistory(this.label,
                    this.date,
                    this.price)

    private fun PriceHistory.toPriceHistoryEntity(symbol: String) =
            PriceHistoryEntity(symbol = symbol,
                    price = this.price,
                    label = this.label,
                    date = this.date)

    private fun Historical.toPriceHistory() =
            PriceHistory(this.label, this.date, this.close)

}