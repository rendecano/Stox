package io.rendecano.stox.list.data.remote.source.impl

import io.rendecano.stox.list.data.remote.api.StockServiceApi
import io.rendecano.stox.list.data.remote.model.CompanyProfile
import io.rendecano.stox.list.data.remote.model.Symbols
import io.rendecano.stox.list.data.remote.source.StockRemoteSource
import retrofit2.Retrofit
import javax.inject.Inject

class StockRemoteDataSource @Inject constructor(retrofit: Retrofit) : StockRemoteSource {

    private val stockServiceApi = retrofit.create(StockServiceApi::class.java)

    override suspend fun getStockList(): List<Symbols> = stockServiceApi.getStockList().symbolsList

    override suspend fun getCompanyProfile(symbol: String): CompanyProfile = stockServiceApi.getCompanyInformation(symbol)

    override suspend fun getCompanyProfiles(symbols: String): List<CompanyProfile> = stockServiceApi.getCompanyInformationByBatch(symbols).companyProfiles
}