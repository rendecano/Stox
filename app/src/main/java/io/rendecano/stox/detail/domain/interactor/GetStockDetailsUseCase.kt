package io.rendecano.stox.detail.domain.interactor

import androidx.lifecycle.LiveData
import io.rendecano.stox.common.domain.interactor.BaseCoroutineUseCase
import io.rendecano.stox.common.domain.model.Either
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.domain.repository.StockRepository
import javax.inject.Inject

class GetStockDetailsUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseCoroutineUseCase<LiveData<Stock>, GetStockDetailsUseCase.Params>() {

    override suspend fun run(params: Params?): Either<Failure, LiveData<Stock>> {
        return try {
            Either.Right(stockRepository.getStockDetails(params!!.symbol))
        } catch (exp: Exception) {
            Either.Left(GetStockDetailsUseCaseFailure(exp))
        }
    }

    data class Params(val symbol: String)

    data class GetStockDetailsUseCaseFailure(val error: Exception) : Failure.FeatureFailure(error)
}