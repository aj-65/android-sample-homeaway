package com.jtigernova.findit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jtigernova.findit.model.Venue

/**
 * View model for favorites
 */
class FavoriteViewModel : ViewModel() {

    val favoriteVenues: MutableLiveData<MutableSet<String>> by lazy {
        MutableLiveData<MutableSet<String>>()
    }

    fun addFavoriteVenue(venue: Venue) {
        favoriteVenues.value?.add(venue.id!!)

        //notify observers
        favoriteVenues.postValue(favoriteVenues.value)
    }

    fun removeFavoriteVenue(venue: Venue) {
        favoriteVenues.value?.remove(venue.id!!)
        //notify observers
        favoriteVenues.postValue(favoriteVenues.value)
    }
}
