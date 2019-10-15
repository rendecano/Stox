package io.rendecano.stox.list.domain.interactor

import io.rendecano.stox.common.domain.interactor.BaseCoroutineUseCase
import io.rendecano.stox.common.domain.model.Either
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.list.domain.model.Status
import io.rendecano.stox.list.domain.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RefreshFavoriteStockListUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseCoroutineUseCase<Int, RefreshFavoriteStockListUseCase.Params>() {

    override suspend fun run(params: Params?): Either<Failure, Int> {
        return try {
            Either.Right(refreshStocks())
        } catch (exp: Exception) {
            Either.Left(RefreshFavoriteStockListUseCaseFailure(exp))
        }
    }

    private suspend fun refreshStocks() = withContext(Dispatchers.IO) {
        val list = stockRepository.getFavoriteStocksList()

        // Download all items with empty percentage changes
        list.forEach {
            val stockProfile = stockRepository.getCompanyProfile(it.symbol, true)
            stockProfile.apply {
                status = when {
                    stockProfile.changesPercentage.contains("+") -> Status.GAIN
                    stockProfile.changesPercentage.contains("-") -> Status.LOSS
                    else -> Status.NO_CHANGE
                }
                isFavorite = true
            }

            stockRepository.updateStock(stockProfile)
        }
        list.size
    }

    class Params

    data class RefreshFavoriteStockListUseCaseFailure(val error: Exception) : Failure.FeatureFailure(error)

}