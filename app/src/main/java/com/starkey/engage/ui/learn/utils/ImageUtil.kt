package com.starkey.engage.ui.learn.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageUtil {

    companion object {
        fun loadVideoThumb(thumbUrl: String, imageView: ImageView){
            Glide.with(imageView).load(thumbUrl).into(imageView)
        }
    }
}