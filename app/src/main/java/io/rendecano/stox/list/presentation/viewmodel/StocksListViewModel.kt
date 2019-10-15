package io.rendecano.stox.list.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import io.rendecano.stox.common.presentation.viewmodel.SingleLiveEvent
import io.rendecano.stox.list.domain.interactor.GetStockInfoUseCase
import io.rendecano.stox.list.domain.interactor.GetStockListUseCase
import io.rendecano.stox.list.domain.interactor.SetStockFavoriteUseCase
import io.rendecano.stox.list.domain.model.Stock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StocksListViewModel @Inject constructor(private val getStockListUseCase: GetStockListUseCase,
                                              private val getStockInfoUseCase: GetStockInfoUseCase,
                                              private val setStockFavoriteUseCase: SetStockFavoriteUseCase) : ViewModel() {

    val loading = SingleLiveEvent<Boolean>()
    val error = SingleLiveEvent<Any>()

    val stockList: LiveData<List<Stock>> = liveData {
        val stockList = getStockListUseCase.execute().value
        emit(stockList ?: listOf())

        stockList?.forEach {
            loadStock(it.symbol)
            emit(getStockListUseCase.execute().value ?: listOf())
        }
    }

    fun updateFavorite(symbol: String, isFavorite: Boolean): LiveData<List<Stock>> = liveData {
        updateStock(symbol, isFavorite)
        emit(getStockListUseCase.execute().value ?: listOf())
    }

    private suspend fun loadStock(symbol: String) = withContext(Dispatchers.IO) {
        getStockInfoUseCase.symbol = symbol
        getStockInfoUseCase.execute().value
    }

    private suspend fun updateStock(symbol: String, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        setStockFavoriteUseCase.isFavorite = isFavorite
        setStockFavoriteUseCase.symbol = symbol
        setStockFavoriteUseCase.execute()
    }
}