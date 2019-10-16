package io.rendecano.stox.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.LineDataSet
import io.rendecano.stox.common.domain.model.Failure
import io.rendecano.stox.common.presentation.viewmodel.SingleLiveEvent
import io.rendecano.stox.detail.domain.interactor.DownloadStockPriceHistoryUseCase
import io.rendecano.stox.detail.domain.interactor.GetStockDetailsUseCase
import io.rendecano.stox.detail.domain.interactor.GetStockPriceHistoryUseCase
import io.rendecano.stox.detail.domain.interactor.RefreshRealtimePriceUseCase
import io.rendecano.stox.detail.domain.model.PriceRangeFilter
import io.rendecano.stox.list.domain.model.Stock
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val REFRESH_TIME: Long = 15 * 1000

class StocksDetailsViewModel @Inject constructor(private val getStockDetailsUseCase: GetStockDetailsUseCase,
                                                 private val getStockPriceHistoryUseCase: GetStockPriceHistoryUseCase,
                                                 private val downloadStockPriceHistoryUseCase: DownloadStockPriceHistoryUseCase,
                                                 private val refreshRealtimePriceUseCase: RefreshRealtimePriceUseCase) : ViewModel() {

    val error = SingleLiveEvent<String>()
    val stockDetails = MediatorLiveData<Stock>()
    val historicalPriceDetails = MediatorLiveData<LineDataSet>()

    private val tickerChannel = ticker(delayMillis = REFRESH_TIME, initialDelayMillis = 0)

    fun getStockDetails(symbol: String) {
        getStockDetailsUseCase.invoke(viewModelScope, GetStockDetailsUseCase.Params(symbol)) { it.either(::handleFailure, ::handleStockDetailsSuccess) }

        viewModelScope.launch {
            startTimer(symbol)
        }
    }

    fun getHistoricalPriceDetails(symbol: String, filter: PriceRangeFilter = PriceRangeFilter.QUARTER) {
        getStockPriceHistoryUseCase.invoke(viewModelScope, GetStockPriceHistoryUseCase.Params(symbol)) { it.either(::handleFailure, ::handleHistoryPriceDetailsSuccess) }
        downloadStockPriceHistoryUseCase.invoke(viewModelScope, DownloadStockPriceHistoryUseCase.Params(symbol, filter))
    }

    private fun handleStockDetailsSuccess(stock: LiveData<Stock>) {
        stockDetails.addSource(stock) { result ->
            result.let {
                stockDetails.value = it
            }
        }
    }

    private fun handleHistoryPriceDetailsSuccess(stock: LiveData<LineDataSet>) {
        historicalPriceDetails.addSource(stock) { result ->
            result.let {
                historicalPriceDetails.value = it
            }
        }
    }


    private fun handleFailure(exception: Failure) {
        error.value = exception.exception.message
    }

    private suspend fun startTimer(symbol: String) {
        for (event in tickerChannel) {
            refreshRealtimePriceUseCase.invoke(viewModelScope, RefreshRealtimePriceUseCase.Params(symbol))
        }
    }

    override fun onCleared() {
        super.onCleared()
        tickerChannel.cancel()
    }
}