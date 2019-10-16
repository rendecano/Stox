package io.rendecano.stox.list.domain.interactor

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.domain.repository.StockRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class RefreshFavoriteRealtimePriceUseCaseTest {
    @Mock
    private lateinit var stockRepository: StockRepository
    lateinit var refreshFavoriteRealtimePriceUseCase: RefreshFavoriteRealtimePriceUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        refreshFavoriteRealtimePriceUseCase = RefreshFavoriteRealtimePriceUseCase(stockRepository)
    }

    @Test
    fun `test refresh realtime price of stocks`() {
        runBlocking {

            // Given
            val stock = Stock(symbol = "REN", price = 200.0)
            val stockList = arrayListOf(stock)
            given(stockRepository.getFavoriteStocksList()).willReturn(stockList)
            given(stockRepository.getRealtimePrice("REN")).willReturn(stock)

            // Whne
            val result = refreshFavoriteRealtimePriceUseCase.run(RefreshFavoriteRealtimePriceUseCase.Params())

            // Then
            verify(stockRepository).getFavoriteStocksList()
            verify(stockRepository).getRealtimePrice("REN")
            verify(stockRepository).updateStock(any())
            verifyNoMoreInteractions(stockRepository)
            Assert.assertTrue(result.isRight)
            result.either({},
                    { right ->
                        Assert.assertTrue(right == 1)
                    })
        }
    }
}