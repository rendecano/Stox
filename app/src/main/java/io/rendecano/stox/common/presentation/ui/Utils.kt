package io.rendecano.stox.common.presentation.ui

import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.hdodenhof.circleimageview.CircleImageView
import io.rendecano.stox.R

const val SYMBOL = "symbol"
const val THEME_PREF = "theme_pref"

@BindingAdapter("stockImageUrl")
fun CircleImageView.bindImageUrl(imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        if (this.getTag(R.id.imgCompany) == null || this.getTag(R.id.imgCompany) != imageUrl) {
            this.setTag(R.id.imgCompany, imageUrl)
            Glide.with(context).load(imageUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(this)
        }
    } else {
        this.setTag(R.id.imgCompany, null)
    }
}

fun Int.stringify(res: Resources): String = res.getString(this)

object ThemeHelper {
    fun applyDarkTheme(isDarkTheme: Boolean) {
        when (isDarkTheme) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}