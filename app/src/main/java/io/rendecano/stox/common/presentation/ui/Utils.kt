package io.rendecano.stox.common.presentation.ui

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.hdodenhof.circleimageview.CircleImageView
import io.rendecano.stox.R

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