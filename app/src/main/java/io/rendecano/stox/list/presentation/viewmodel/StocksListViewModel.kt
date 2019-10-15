package io.rendecano.stox.list.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.common.presentation.viewmodel.SingleLiveEvent
import io.rendecano.stox.list.domain.interactor.DownloadStockListUseCase
import io.rendecano.stox.list.domain.interactor.GetStockListUseCase
import io.rendecano.stox.list.domain.interactor.SetStockFavoriteUseCase
import io.rendecano.stox.list.domain.model.Stock
import javax.inject.Inject

class StocksListViewModel @Inject constructor(private val getStockListUseCase: GetStockListUseCase,
                                              private val downloadStockListUseCase: DownloadStockListUseCase,
                                              private val setStockFavoriteUseCase: SetStockFavoriteUseCase) : ViewModel() {

    val error = SingleLiveEvent<String>()
    val stockList = MediatorLiveData<List<Stock>>()

    fun updateFavorite(symbol: String, isFavorite: Boolean) {
        setStockFavoriteUseCase.invoke(viewModelScope, SetStockFavoriteUseCase.Params(symbol, isFavorite))
    }

    fun getAllStocks() {
        getStockListUseCase.invoke(viewModelScope, GetStockListUseCase.Params()) { it.either(::handleFailure, ::handleSuccess) }
        downloadStockListUseCase.invoke(viewModelScope, null)
    }

    private fun handleSuccess(list: LiveData<List<Stock>>) {
        stockList.addSource(list) { result ->
            result.let { stockList.value = it }
        }
    }

    private fun handleFailure(exception: Failure) {
        error.value = exception.exception.message
    }
}