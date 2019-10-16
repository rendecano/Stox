package io.rendecano.stox.list.domain.interactor

import io.rendecano.stox.common.domain.interactor.BaseUseCase
import io.rendecano.stox.common.domain.model.Either
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.list.domain.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RefreshFavoriteRealtimePriceUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseUseCase<Int, RefreshFavoriteRealtimePriceUseCase.Params>() {

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
        list.forEach { stock ->
            val updatedStock = stockRepository.getRealtimePrice(stock.symbol)
            stock.apply { price = updatedStock.price }
            stockRepository.updateStock(stock)
        }
        list.size
    }

    class Params

    data class RefreshFavoriteStockListUseCaseFailure(val error: Exception) : Failure.FeatureFailure(error)

}