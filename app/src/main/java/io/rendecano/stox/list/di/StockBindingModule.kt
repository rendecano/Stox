package io.rendecano.stox.list.di

import dagger.Module
import dagger.Provides
import io.rendecano.stox.common.data.local.AppDatabase
import io.rendecano.stox.list.data.local.source.StockLocalSource
import io.rendecano.stox.list.data.local.source.impl.StockLocalDataSource
import io.rendecano.stox.list.data.remote.source.StockRemoteSource
import io.rendecano.stox.list.data.remote.source.impl.StockRemoteDataSource
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class StockBindingModule {

    @Provides
    @Singleton
    internal fun provideStockRemoteSource(retrofit: Retrofit): StockRemoteSource {
        return StockRemoteDataSource(retrofit)
    }

    @Provides
    @Singleton
    internal fun provideStockLocalSource(appDatabase: AppDatabase): StockLocalSource {
        return StockLocalDataSource(appDatabase)
    }
}