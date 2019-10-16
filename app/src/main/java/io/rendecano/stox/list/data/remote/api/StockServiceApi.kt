package io.rendecano.stox.list.data.remote.api


import io.rendecano.stox.list.data.remote.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StockServiceApi {
    @GET("api/v3/company/stock/list")
    suspend fun getStockList(): StockList

    @GET("api/v3/company/profile/{symbol}")
    suspend fun getCompanyInformation(@Path("symbol") symbol: String): CompanyProfile

    @GET("api/v3/stock/real-time-price/{symbol}")
    suspend fun getRealtimePrice(@Path("symbol") symbol: String): Symbols

    @GET("api/v3/historical-price-full/{symbol}")
    suspend fun getHistoricalPriceList(@Path("symbol") symbol: String,
                                   @Query("from") fromDate: String,
                                   @Query("to") toDate: String): HistoricalPriceList
}