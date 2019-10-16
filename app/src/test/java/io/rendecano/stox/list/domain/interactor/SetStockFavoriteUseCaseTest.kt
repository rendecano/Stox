package io.rendecano.stox.list.domain.interactor

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.domain.repository.StockRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class SetStockFavoriteUseCaseTest {
    @Mock
    private lateinit var stockRepository: StockRepository
    lateinit var setStockFavoriteUseCase: SetStockFavoriteUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        setStockFavoriteUseCase = SetStockFavoriteUseCase(stockRepository)
    }

    @Test
    fun `test set stock as favorite`() = runBlocking {
        // Given
        val stock = Stock(symbol = "REN", price = 200.0)
        given(stockRepository.getCompanyProfile("REN")).willReturn(stock)

        // When
        setStockFavoriteUseCase.run(SetStockFavoriteUseCase.Params("REN", true))

        // Then
        verify(stockRepository).getCompanyProfile("REN")
        verify(stockRepository).updateStock(any())
        verifyNoMoreInteractions(stockRepository)
    }
}