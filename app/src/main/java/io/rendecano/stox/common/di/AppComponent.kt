package io.rendecano.stox.common.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import io.rendecano.stox.StoxApplication
import io.rendecano.stox.list.di.StockBindingModule
import io.rendecano.stox.list.di.StockModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            (AndroidSupportInjectionModule::class),
            (AppModule::class),
            (AppBindingModule::class),
            (StockModule::class),
            (StockBindingModule::class)
        ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: StoxApplication)
}