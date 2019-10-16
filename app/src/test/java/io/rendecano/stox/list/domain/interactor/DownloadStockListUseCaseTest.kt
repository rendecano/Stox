package io.rendecano.stox.list.domain.interactor

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import io.rendecano.stox.list.domain.model.Stock
import io.rendecano.stox.list.domain.repository.StockRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class DownloadStockListUseCaseTest {

    @Mock
    private lateinit var stockRepository: StockRepository
    lateinit var downloadStockListUseCase: DownloadStockListUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        downloadStockListUseCase = DownloadStockListUseCase(stockRepository)
    }

    @Test
    fun `test download stocks`() {
        runBlocking {

            // Given
            val stock = Stock(symbol = "REN", price = 200.0)
            val stockList = arrayListOf(stock)
            given(stockRepository.downloadStockList()).willReturn(stockList)
            given(stockRepository.getCompanyProfile("REN")).willReturn(stock)

            // When
            val result = downloadStockListUseCase.run(DownloadStockListUseCase.Params())

            // Then
            verify(stockRepository).downloadStockList()
            verify(stockRepository).saveStockList(stockList)
            verify(stockRepository).getCompanyProfile("REN")
            verify(stockRepository).updateStock(any())
            verifyNoMoreInteractions(stockRepository)
            assertTrue(result.isRight)
            result.either({},
                    { right ->
                        assertTrue(right == 1)
                    })
        }
    }
}