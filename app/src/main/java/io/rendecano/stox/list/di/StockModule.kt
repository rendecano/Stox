package io.rendecano.stox.list.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.rendecano.stox.common.di.ViewModelKey
import io.rendecano.stox.list.data.repository.StockDataRepository
import io.rendecano.stox.list.domain.repository.StockRepository
import io.rendecano.stox.list.presentation.ui.FavoriteStocksListFragment
import io.rendecano.stox.list.presentation.ui.StocksListFragment
import io.rendecano.stox.list.presentation.viewmodel.FavoritesStocksListViewModel
import io.rendecano.stox.list.presentation.viewmodel.StocksListViewModel

@Module
abstract class StockModule {

    @ContributesAndroidInjector
    abstract fun contributeStockListFragment(): StocksListFragment

    @ContributesAndroidInjector
    abstract fun contributeFavoriteStockListFragment(): FavoriteStocksListFragment

    @Binds
    @IntoMap
    @ViewModelKey(StocksListViewModel::class)
    internal abstract fun bindStocksListViewModel(stocksListViewModel: StocksListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesStocksListViewModel::class)
    internal abstract fun bindFavoritesStocksListViewModel(favoritesStocksListViewModel: FavoritesStocksListViewModel): ViewModel

    @Binds
    abstract fun bindStockRepository(stockDataRepository: StockDataRepository): StockRepository
}