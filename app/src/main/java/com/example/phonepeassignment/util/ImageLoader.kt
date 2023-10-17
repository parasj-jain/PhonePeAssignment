package com.example.phonepeassignment.util

import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageLoader {

    fun setImage(
        imageView: ImageView,
        url: String?
    ) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }

}