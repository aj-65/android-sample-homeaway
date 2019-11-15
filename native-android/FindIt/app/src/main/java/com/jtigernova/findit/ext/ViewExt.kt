package com.jtigernova.findit.ext

import android.content.Context
import android.widget.CheckBox
import com.jtigernova.findit.R
import com.jtigernova.findit.model.Venue
import com.jtigernova.findit.viewmodel.FavoriteViewModel

fun CheckBox.updateFavoriteText(context: Context) {
    if (isChecked) {
        text = context.getString(R.string.saved_favorite)
    } else {
        text = context.getString(R.string.add_favorite)
    }
}

fun CheckBox.setupFavorite(context: Context, favViewModel: FavoriteViewModel,
                           venue: Venue, onCheckedChange: ((checked: Boolean) -> Unit)? = null) {
    updateFavoriteText(context)

    setOnCheckedChangeListener { _, checked ->
        if (checked) {
            favViewModel.addFavoriteVenue(venue)
        } else {
            favViewModel.removeFavoriteVenue(venue)
        }

        updateFavoriteText(context)

        onCheckedChange?.invoke(checked)
    }
}