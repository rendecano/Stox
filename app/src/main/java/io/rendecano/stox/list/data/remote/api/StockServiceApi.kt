package io.rendecano.stox.list.data.remote.api


import io.rendecano.stox.list.data.remote.model.CompanyProfile
import io.rendecano.stox.list.data.remote.model.CompanyProfileList
import io.rendecano.stox.list.data.remote.model.StockList
import retrofit2.http.GET
import retrofit2.http.Path

interface StockServiceApi {
    @GET("api/v3/company/stock/list")
    suspend fun getStockList(): StockList

    @GET("api/v3/company/profile/{symbol}")
    suspend fun getCompanyInformation(@Path("symbol") symbol: String): CompanyProfile
}