package io.rendecano.stox.list.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.common.presentation.viewmodel.SingleLiveEvent
import io.rendecano.stox.list.domain.interactor.GetFavoriteStockListUseCase
import io.rendecano.stox.list.domain.interactor.RefreshFavoriteRealtimePriceUseCase
import io.rendecano.stox.list.domain.interactor.SetStockFavoriteUseCase
import io.rendecano.stox.list.domain.model.Stock
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val REFRESH_TIME: Long = 15 * 1000

class FavoritesStocksListViewModel @Inject constructor(private val getFavoriteStockListUseCase: GetFavoriteStockListUseCase,
                                                       private val setStockFavoriteUseCase: SetStockFavoriteUseCase,
                                                       private val refreshFavoriteRealtimePriceUseCase: RefreshFavoriteRealtimePriceUseCase) : ViewModel() {

    val error = SingleLiveEvent<String>()
    val favoriteStockList = MediatorLiveData<List<Stock>>()

    private val tickerChannel = ticker(delayMillis = REFRESH_TIME, initialDelayMillis = 0)

    fun getFavorites() {
        getFavoriteStockListUseCase.invoke(viewModelScope, GetFavoriteStockListUseCase.Params()) { it.either(::handleFailure, ::handleSuccess) }
    }

    fun updateFavorite(symbol: String, isFavorite: Boolean) {
        setStockFavoriteUseCase.invoke(viewModelScope, SetStockFavoriteUseCase.Params(symbol, isFavorite))
    }

    private fun handleSuccess(list: LiveData<List<Stock>>) {
        favoriteStockList.addSource(list) { result ->
            result.let {
                favoriteStockList.value = it
            }
        }

        viewModelScope.launch {
            startTimer()
        }
    }

    private fun handleFailure(exception: Failure) {
        error.value = exception.exception.message
    }

    private suspend fun startTimer() {
        for (event in tickerChannel) {
            refreshFavoriteRealtimePriceUseCase.invoke(viewModelScope, RefreshFavoriteRealtimePriceUseCase.Params())
        }
    }

    override fun onCleared() {
        super.onCleared()
        tickerChannel.cancel()
    }
}