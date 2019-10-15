package io.rendecano.stox.list.domain.interactor

import io.rendecano.stox.common.domain.interactor.BaseCoroutineUseCase
import io.rendecano.stox.common.domain.model.Either
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.list.domain.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetStockFavoriteUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseCoroutineUseCase<Any, SetStockFavoriteUseCase.Params>() {

    override suspend fun run(params: Params?): Either<Failure, Any> {
        return try {
            Either.Right(setAsFavorite(params!!.symbol, params.isFavorite))
        } catch (exp: Exception) {
            Either.Left(SetStockFavoriteUseCaseFailure(exp))
        }
    }

    private suspend fun setAsFavorite(symbol: String, updatedValue: Boolean) = withContext(Dispatchers.IO) {
        val stockProfile = stockRepository.getCompanyProfile(symbol)

        stockProfile.apply {
            isFavorite = updatedValue
        }

        stockRepository.updateStock(stockProfile)
    }


    data class Params(val symbol: String, val isFavorite: Boolean)

    data class SetStockFavoriteUseCaseFailure(val error: Exception) : Failure.FeatureFailure(error)
}