package io.rendecano.stox.detail.domain.interactor

import io.rendecano.stox.common.domain.interactor.BaseCoroutineUseCase
import io.rendecano.stox.common.domain.model.Either
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.list.domain.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RefreshRealtimePriceUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseCoroutineUseCase<Any, RefreshRealtimePriceUseCase.Params>() {

    override suspend fun run(params: Params?): Either<Failure, Any> {
        return try {
            Either.Right(refreshStock(params!!.symbol))
        } catch (exp: Exception) {
            Either.Left(RefreshFavoriteStockListUseCaseFailure(exp))
        }
    }

    private suspend fun refreshStock(symbol: String) = withContext(Dispatchers.IO) {
        val stock = stockRepository.getCompanyProfile(symbol)
        val updatedStock = stockRepository.getRealtimePrice(symbol)
        stock.apply { price = updatedStock.price }
        stockRepository.updateStock(stock)
    }

    data class Params(val symbol: String)

    data class RefreshFavoriteStockListUseCaseFailure(val error: Exception) : Failure.FeatureFailure(error)

}