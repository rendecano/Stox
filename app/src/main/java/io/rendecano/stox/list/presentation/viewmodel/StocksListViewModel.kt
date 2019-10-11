package io.rendecano.stox.list.presentation.viewmodel

import androidx.lifecycle.ViewModel
import io.rendecano.stox.common.presentation.viewmodel.BaseAction
import io.rendecano.stox.common.presentation.viewmodel.SingleLiveEvent
import io.rendecano.stox.list.domain.interactor.GetStockListUseCase
import io.rendecano.stox.list.domain.model.Stock
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import javax.inject.Inject

class StocksListViewModel @Inject constructor(private val getStockListUseCase: GetStockListUseCase) : ViewModel() {

    val loading = SingleLiveEvent<Boolean>()
    val error = SingleLiveEvent<Any>()
    val stockList = SingleLiveEvent<List<Stock>>()

    fun action(action: GetStockListAction) = actor.offer(action)

    private val actor =
            GlobalScope.actor<GetStockListAction>(
                    Dispatchers.Main,
                    Channel.CONFLATED,
                    CoroutineStart.DEFAULT,
                    null,
                    {

                        for (action in this) when (action) {
                            is GetStockListAction.Init -> {
                                loading.value = true
                                stockList.value = getStockListUseCase.execute()
                                loading.value = false
                            }
                        }
                    })

    sealed class GetStockListAction : BaseAction {
        object Init : GetStockListAction()
    }
}