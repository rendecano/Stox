package io.rendecano.stox.list.domain.interactor

import io.rendecano.stox.common.domain.interactor.BaseCoroutineUseCase
import io.rendecano.stox.common.domain.model.Either
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.list.domain.model.Status
import io.rendecano.stox.list.domain.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadStockListUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseCoroutineUseCase<Int, DownloadStockListUseCase.Params>() {

    override suspend fun run(params: Params?): Either<Failure, Int> {
        return try {
            Either.Right(downloadStocks())
        } catch (exp: Exception) {
            Either.Left(DownloadStockListUseCaseFailure(exp))
        }
    }

    private suspend fun downloadStocks() = withContext(Dispatchers.IO) {
        val list = stockRepository.downloadStockList()
        stockRepository.saveStockList(list)

        // Download all items with empty percentage changes
        list.filter { it.changesPercentage.isEmpty() }.forEach {
            val stockProfile = stockRepository.getCompanyProfile(it.symbol)

            stockProfile.apply {
                status = when {
                    stockProfile.changesPercentage.contains("+") -> Status.GAIN
                    stockProfile.changesPercentage.contains("-") -> Status.LOSS
                    else -> Status.NO_CHANGE
                }
            }

            stockRepository.updateStock(stockProfile)
        }
        list.size
    }

    data class Params(val maxNumberOfFriends: Int)

    data class DownloadStockListUseCaseFailure(val error: Exception) : Failure.FeatureFailure(error)
}