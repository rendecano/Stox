package io.rendecano.stox.detail.domain.interactor

import io.rendecano.stox.common.domain.interactor.BaseUseCase
import io.rendecano.stox.common.domain.model.Either
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.detail.domain.model.PriceRangeFilter
import io.rendecano.stox.list.domain.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class DownloadStockPriceHistoryUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseUseCase<Int, DownloadStockPriceHistoryUseCase.Params>() {

    override suspend fun run(params: Params?): Either<Failure, Int> {
        return try {
            val dateFrom = getDateString(params!!.priceRangeFilter)
            val dateTo = getDateString(isToday = true)

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

    private fun getDateString(priceRangeFilter: PriceRangeFilter = PriceRangeFilter.QUARTER, isToday: Boolean = false): String {
        val now = Calendar.getInstance()

        if (!isToday) {
            when (priceRangeFilter) {
                PriceRangeFilter.SEMI_ANNUAL -> now.add(Calendar.MONTH, -6)
                PriceRangeFilter.ANNUAL -> now.add(Calendar.YEAR, -1)
                PriceRangeFilter.THREE_YEARS -> now.add(Calendar.YEAR, -3)
                else -> now.add(Calendar.MONTH, -3)
            }
        }

        val year = now.get(Calendar.YEAR)
        val monthOfYear = now.get(Calendar.MONTH)
        val dayOfMonth = now.get(Calendar.DAY_OF_MONTH)

        val month = if (monthOfYear + 1 > 9) "" + (monthOfYear + 1) else "0" + (monthOfYear + 1)
        val day = if (dayOfMonth > 9) "" + dayOfMonth else "0$dayOfMonth"

        return "$year-$month-$day"
    }

    data class Params(val symbol: String, val priceRangeFilter: PriceRangeFilter)

    data class DownloadStockListUseCaseFailure(val error: Exception) : Failure.FeatureFailure(error)
}