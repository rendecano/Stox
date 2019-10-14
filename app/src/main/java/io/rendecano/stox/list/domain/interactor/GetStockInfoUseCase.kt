package io.rendecano.stox.list.domain.interactor

import androidx.lifecycle.MutableLiveData
import io.rendecano.stox.common.domain.interactor.BaseUseCase
import io.rendecano.stox.list.domain.model.Status
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.domain.repository.StockRepository
import javax.inject.Inject

class GetStockInfoUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseUseCase<MutableLiveData<Stock>>() {

    var symbol = ""
    override suspend fun execute(): MutableLiveData<Stock> = asyncAwait {
        try {
            val data = MutableLiveData<Stock>()

            val stockProfile = stockRepository.getCompanyProfile(symbol)
            var buraot = Stock()
            when {
                stockProfile.changesPercentage.contains("+") -> buraot = stockProfile.copy(status = Status.GAIN)
                stockProfile.changesPercentage.contains("-") -> buraot = stockProfile.copy(status = Status.LOSS)
                else ->  buraot = stockProfile.copy(status = Status.NO_CHANGE)
            }

            stockRepository.updateStock(stockProfile)

            data.postValue(buraot)
            data

        } catch (exception: Exception) {
            MutableLiveData<Stock>()
        }
    }
}