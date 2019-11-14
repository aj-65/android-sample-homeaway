package com.jtigernova.findit.ui

import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jtigernova.findit.R
import kotlin.math.roundToInt

fun Context.loadImgInto(uri: String?, imageView: ImageView) {
    Glide.with(this)
            .load(uri)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(imageView)
}

fun Context.dpToPixels(dpRes: Int): Int {
    val dip = resources.getDimension(dpRes)
    val r = resources
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            r.displayMetrics
    ).roundToInt()
}