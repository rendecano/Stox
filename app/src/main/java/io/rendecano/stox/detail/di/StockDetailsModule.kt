package io.rendecano.stox.detail.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.rendecano.stox.common.di.ViewModelKey
import io.rendecano.stox.detail.presentation.ui.StockDetailsActivity
import io.rendecano.stox.detail.presentation.viewmodel.StocksDetailsViewModel

@Module
abstract class StockDetailsModule {

    @ContributesAndroidInjector
    abstract fun contributeStockDetailsActivity(): StockDetailsActivity

    @Binds
    @IntoMap
    @ViewModelKey(StocksDetailsViewModel::class)
    internal abstract fun bindStocksDetailsViewModel(stocksDetailsViewModel: StocksDetailsViewModel): ViewModel
}