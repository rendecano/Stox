package io.rendecano.stox.setting

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.rendecano.stox.BuildConfig
import io.rendecano.stox.R
import io.rendecano.stox.common.presentation.ui.THEME_PREF
import io.rendecano.stox.common.presentation.ui.ThemeHelper
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        switchDarkTheme.isChecked = sharedPreferences.getBoolean(THEME_PREF, false)
        switchDarkTheme.setOnCheckedChangeListener { view, isChecked ->
            if (view.isPressed) {
                ThemeHelper.applyDarkTheme(isChecked)
                sharedPreferences.edit().apply {
                    putBoolean(THEME_PREF, isChecked)
                }.apply()
            }
        }

        val versionName = BuildConfig.VERSION_NAME
        val versionCode = BuildConfig.VERSION_CODE
        txtVersion.text = getString(R.string.version).format(versionName, versionCode)
    }
}