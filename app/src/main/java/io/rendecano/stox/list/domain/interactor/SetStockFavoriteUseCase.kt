package io.rendecano.stox.list.domain.interactor

import io.rendecano.stox.common.domain.interactor.BaseUseCase
import io.rendecano.stox.list.domain.repository.StockRepository
import javax.inject.Inject

class SetStockFavoriteUseCase @Inject constructor(private val stockRepository: StockRepository) : BaseUseCase<Any>() {

    var symbol = ""
    var isFavorite = false
    override suspend fun execute(): Any = asyncAwait {
        try {
            val stockProfile = stockRepository.getCompanyProfile(symbol).copy(isFavorite = isFavorite)
            stockRepository.updateStock(stockProfile)
        } catch (exception: Exception) {
            exception.printStackTrace()
            Any()
        }
    }
}