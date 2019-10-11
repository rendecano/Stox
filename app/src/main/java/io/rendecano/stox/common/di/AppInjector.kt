package io.rendecano.stox.common.di

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import io.rendecano.stox.StoxApplication

object AppInjector {

    fun init(app: StoxApplication) {

        DaggerAppComponent.builder().application(app).build().inject(app)

        app.registerActivityLifecycleCallbacks(object : LifecycleHandlerComponentAdapter() {
            override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                injectActivity(activity)
            }
        })
    }

    private fun injectActivity(activity: Activity) {

        // Inject activities
        if (activity is Injectable) {
            AndroidInjection.inject(activity)
        }

        // Inject fragments
        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                    .registerFragmentLifecycleCallbacks(
                            object : FragmentManager.FragmentLifecycleCallbacks() {

                                override fun onFragmentPreAttached(fm: FragmentManager, fragment: Fragment, context: Context) {
                                    if (fragment is Injectable) {
                                        AndroidSupportInjection.inject(fragment)
                                    }
                                    super.onFragmentPreAttached(fm, fragment, context)
                                }

                            }, true
                    )
        }
    }
}