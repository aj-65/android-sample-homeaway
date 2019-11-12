package com.jtigernova.findit.ui

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jtigernova.findit.R

fun Context.loadImgInto(uri: String?, imageView: ImageView) {
    Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(imageView)
}