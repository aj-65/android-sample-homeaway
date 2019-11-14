package com.jtigernova.findit.ext

import android.content.Context
import android.widget.CheckBox
import com.jtigernova.findit.R

fun CheckBox.updateFavoriteText(context: Context) {
    if (isChecked) {
        text = context.getString(R.string.saved_favorite)
    } else {
        text = context.getString(R.string.add_favorite)
    }
}