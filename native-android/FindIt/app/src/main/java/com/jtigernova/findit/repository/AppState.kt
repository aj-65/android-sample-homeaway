package com.jtigernova.findit.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.jtigernova.findit.viewmodel.FavoriteViewModel

/**
 * Class to handle app state
 */
object AppState {
    val favoriteViewModel = FavoriteViewModel()

    /**
     * Loads view models from disk
     */
    fun load(context: Context) {
        favoriteViewModel.favoriteVenues.value = Prefs.getFavoriteVenues(context).toMutableSet()
    }

    /**
     * Saves the view models to disk
     */
    fun persist(context: Context) {
        Prefs.saveFavoriteVenues(context, favoriteViewModel.favoriteVenues.value!!.toList())
    }

    /**
     * Class to handle saving data to device
     */
    private object Prefs {
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

        fun getFavoriteVenues(context: Context): MutableList<String> {
            val prefs = get(context)

            if (prefs.contains(KEY_FAV_VENUES))
                return gson.fromJson(prefs.getString(KEY_FAV_VENUES, "")!!,
                        Array<String>::class.java).toMutableList()

            return mutableListOf()
        }
    }
}