package io.rendecano.stox

import android.app.Application
import android.preference.PreferenceManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.rendecano.stox.common.di.AppInjector
import io.rendecano.stox.common.presentation.ui.THEME_PREF
import io.rendecano.stox.common.presentation.ui.ThemeHelper
import javax.inject.Inject

class StoxApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val themePref = sharedPreferences.getBoolean(THEME_PREF, false)
        ThemeHelper.applyDarkTheme(themePref)

        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}