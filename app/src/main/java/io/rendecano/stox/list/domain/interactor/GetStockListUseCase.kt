package io.rendecano.stox.list.domain.interactor

import io.rendecano.stox.common.domain.interactor.BaseUseCase
import io.rendecano.stox.list.domain.model.Status
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.domain.repository.StockRepository
import javax.inject.Inject

class GetStockListUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseUseCase<List<Stock>>() {

    override suspend fun execute(): List<Stock> = asyncAwait {
        try {
            var stockSymbols = ""
            stockRepository.getStockList().take(3).forEach {
                stockSymbols += "${it.symbol},"
            }

            val stockList = stockRepository.getCompanyProfiles(stockSymbols.removeSuffix(","))
            stockList.map {
                apply {
                    when {
                        it.changesPercentage.contains("+") -> it.status = Status.GAIN
                        it.changesPercentage.contains("-") -> it.status = Status.LOSS
                        else -> it.status = Status.NO_CHANGE
                    }
                }
            }

            stockRepository.saveStockList(stockList)

            // TODO: Investigate on how to use pagination and live data for realtime updates
            stockList
        } catch (exception: Exception) {
            listOf<Stock>()
        }
    }
}