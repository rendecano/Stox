package io.rendecano.stox.list.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import io.rendecano.stox.common.presentation.viewmodel.SingleLiveEvent
import io.rendecano.stox.list.domain.interactor.GetStockInfoUseCase
import io.rendecano.stox.list.domain.interactor.GetStockListUseCase
import io.rendecano.stox.list.domain.model.Stock
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StocksListViewModel @Inject constructor(private val getStockListUseCase: GetStockListUseCase, private val getStockInfoUseCase: GetStockInfoUseCase) : ViewModel() {

    val loading = SingleLiveEvent<Boolean>()
    val error = SingleLiveEvent<Any>()

    var stockList: LiveData<List<Stock>> = liveData {
        emit(getStockListUseCase.execute().value ?: listOf())

        getStockListUseCase.execute().value?.forEach {
            loadStock(it.symbol)
            emit(getStockListUseCase.execute().value ?: listOf())
        }
    }

    suspend fun loadStock(symbol: String): Stock = withContext(Dispatchers.IO) {
        val deferred = CompletableDeferred<Stock>()
        val stock = withContext(Dispatchers.IO) {
            getStockInfoUseCase.symbol = symbol
            getStockInfoUseCase.execute().value
        } ?: Stock()

        deferred.complete(stock)
        deferred.await()
    }
}