package io.rendecano.stox.detail.domain.interactor

import io.rendecano.stox.common.domain.interactor.BaseUseCase
import io.rendecano.stox.common.domain.model.Either
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.detail.domain.model.PriceRangeFilter
import io.rendecano.stox.list.domain.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadStockPriceHistoryUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseUseCase<Int, DownloadStockPriceHistoryUseCase.Params>() {

    override suspend fun run(params: Params?): Either<Failure, Int> {
        return try {

            // TODO: Format current date to get the price range
            val dateFrom = when (params!!.priceRangeFilter) {
                PriceRangeFilter.SEMI_ANNUAL -> "2019-04-15"
                PriceRangeFilter.ANNUAL -> "2018-10-15"
                PriceRangeFilter.THREE_YEARS -> "2016-10-15"
                else -> "2019-07-15"
            }

            val dateTo = "2019-10-15"

            Either.Right(downloadStockPriceHistory(params.symbol, dateFrom, dateTo))
        } catch (exp: Exception) {
            Either.Left(DownloadStockListUseCaseFailure(exp))
        }
    }

    private suspend fun downloadStockPriceHistory(symbol: String, dateFrom: String, dateTo: String) = withContext(Dispatchers.IO) {
        val list = stockRepository.getHistoricalPrice(symbol, dateFrom, dateTo)
        stockRepository.saveHistoricalPriceList(symbol, list)
        list.size
    }

    data class Params(val symbol: String, val priceRangeFilter: PriceRangeFilter)

    data class DownloadStockListUseCaseFailure(val error: Exception) : Failure.FeatureFailure(error)
}