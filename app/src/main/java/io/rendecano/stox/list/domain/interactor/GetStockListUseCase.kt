package io.rendecano.stox.list.domain.interactor

import androidx.lifecycle.MutableLiveData
import io.rendecano.stox.common.domain.interactor.BaseUseCase
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.domain.repository.StockRepository
import javax.inject.Inject

class GetStockListUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseUseCase<MutableLiveData<List<Stock>>>() {

    override suspend fun execute(): MutableLiveData<List<Stock>> = asyncAwait {
        try {
            val data = MutableLiveData<List<Stock>>()
            data.postValue(stockRepository.getStockList())
            data
        } catch (exception: Exception) {
            MutableLiveData<List<Stock>>()
        }
    }
}