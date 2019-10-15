package io.rendecano.stox.list.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import io.rendecano.stox.common.presentation.viewmodel.SingleLiveEvent
import io.rendecano.stox.list.domain.interactor.GetFavoriteStockListUseCase
import io.rendecano.stox.list.domain.interactor.SetStockFavoriteUseCase
import io.rendecano.stox.list.domain.model.Stock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoritesStocksListViewModel @Inject constructor(private val getFavoriteStockListUseCase: GetFavoriteStockListUseCase,
                                                       private val setStockFavoriteUseCase: SetStockFavoriteUseCase) : ViewModel() {

    val loading = SingleLiveEvent<Boolean>()
    val error = SingleLiveEvent<Any>()

    val favoriteStockList: LiveData<List<Stock>> = liveData {
        emit(getFavoriteStockListUseCase.execute().value ?: listOf())
    }

    fun updateFavorite(symbol: String, isFavorite: Boolean): LiveData<List<Stock>> = liveData {
        updateStock(symbol, isFavorite)
        emit(getFavoriteStockListUseCase.execute().value ?: listOf())
    }

    private suspend fun updateStock(symbol: String, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        setStockFavoriteUseCase.isFavorite = isFavorite
        setStockFavoriteUseCase.symbol = symbol
        setStockFavoriteUseCase.execute()
    }
}