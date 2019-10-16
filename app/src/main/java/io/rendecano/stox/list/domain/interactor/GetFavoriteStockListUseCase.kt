package io.rendecano.stox.list.domain.interactor

import androidx.lifecycle.LiveData
import io.rendecano.stox.common.domain.interactor.BaseUseCase
import io.rendecano.stox.common.domain.model.Either
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.domain.repository.StockRepository
import javax.inject.Inject

class GetFavoriteStockListUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseUseCase<LiveData<List<Stock>>, GetFavoriteStockListUseCase.Params>() {

    override suspend fun run(params: Params?): Either<Failure, LiveData<List<Stock>>> {
        return try {
            Either.Right(stockRepository.getFavoriteStockList())
        } catch (exp: Exception) {
            Either.Left(GetStockListCoroutineFailure(exp))
        }
    }

    class Params

    data class GetStockListCoroutineFailure(val error: Exception) : Failure.FeatureFailure(error)
}