package io.rendecano.stox.common.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.rendecano.stox.MainActivity

@Module
abstract class AppModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @Binds
    abstract fun bindContext(application: Application): Context

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory
}