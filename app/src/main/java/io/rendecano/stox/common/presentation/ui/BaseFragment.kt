package io.rendecano.stox.common.presentation.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import io.rendecano.stox.common.di.Injectable
import javax.inject.Inject

open class BaseFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    fun <T> LiveData<T>.observe(observe: (T?) -> Unit) =
            observe(activity!!, Observer { observe(it) })
}