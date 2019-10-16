package io.rendecano.stox.detail.domain.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import io.rendecano.stox.common.domain.interactor.BaseCoroutineUseCase
import io.rendecano.stox.common.domain.model.Either
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.detail.domain.model.PriceHistory
import io.rendecano.stox.list.domain.repository.StockRepository
import javax.inject.Inject

class GetStockPriceHistoryUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseCoroutineUseCase<LiveData<LineDataSet>, GetStockPriceHistoryUseCase.Params>() {

    override suspend fun run(params: Params?): Either<Failure, LiveData<LineDataSet>> {
        return try {
            val lineDataSet = Transformations.map(stockRepository.getPriceHistory(params!!.symbol), ::transform)
            Either.Right(lineDataSet)
        } catch (exp: Exception) {
            Either.Left(GetStockDetailsUseCaseFailure(exp))
        }
    }

    private fun transform(listPriceHistory: List<PriceHistory>): LineDataSet {
        var count = 0
        val valueList = arrayListOf<Entry>()
        listPriceHistory.forEach {
            valueList.add(Entry(count++.toFloat(), it.price.toFloat(), it.date))
        }

        return LineDataSet(valueList, "HISTORY")
    }

    data class Params(val symbol: String)

    data class GetStockDetailsUseCaseFailure(val error: Exception) : Failure.FeatureFailure(error)
}