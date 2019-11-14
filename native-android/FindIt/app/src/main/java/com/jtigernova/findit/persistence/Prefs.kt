package com.jtigernova.findit.persistence

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object Prefs {
    private const val NAME = "FindIt"

    private const val KEY_FAV_VENUES = "fav.venues"

    private fun get(context: Context): SharedPreferences {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun saveFavoriteVenues(context: Context, favorites: Set<String>) {
        get(context).edit {
            putStringSet(KEY_FAV_VENUES, favorites)
            commit()
        }
    }

    fun getFavoriteVenues(context: Context): MutableSet<String> {
        return get(context).getStringSet(KEY_FAV_VENUES, mutableSetOf())!!
    }

    fun isFavoriteVenue(context: Context, venueId: String): Boolean {
        return get(context).getStringSet(KEY_FAV_VENUES, mutableSetOf())!!.contains(venueId)
    }
}