package com.jtigernova.findit.persistence

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

object Prefs {
    private const val NAME = "FindIt"

    private const val KEY_FAV_VENUES = "favorite.venues"

    private var preferences: SharedPreferences? = null

    private val gson = Gson()

    private fun get(context: Context): SharedPreferences {
        if (preferences == null)
            preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

        return preferences!!
    }

    fun saveFavoriteVenues(context: Context, favorites: Iterable<String>) {
        with(get(context).edit()) {
            putString(KEY_FAV_VENUES, gson.toJson(favorites))
            apply()
        }
    }

    fun getFavoriteVenues(context: Context): Iterable<String> {
        val prefs = get(context)

        if (prefs.contains(KEY_FAV_VENUES))
            return gson.fromJson(prefs.getString(KEY_FAV_VENUES, "")!!,
                    Array<String>::class.java).toList()

        return listOf()
    }

    fun isFavoriteVenue(context: Context, venueId: String): Boolean {
        return getFavoriteVenues(context).contains(venueId)
    }
}