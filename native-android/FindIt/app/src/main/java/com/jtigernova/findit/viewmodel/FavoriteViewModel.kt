package com.jtigernova.findit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jtigernova.findit.model.Venue

class FavoriteViewModel : ViewModel() {

    val favoriteVenues: MutableLiveData<MutableSet<String>> by lazy {
        //        DataRepository.getFavoriteVenueIds()
        MutableLiveData<MutableSet<String>>()
    }

    fun addFavoriteVenue(venue: Venue) {
        favoriteVenues.value?.add(venue.id!!)

        //notify observers
        favoriteVenues.value = favoriteVenues.value
    }

    fun removeFavoriteVenue(venue: Venue) {
        favoriteVenues.value?.remove(venue.id!!)

        //notify observers
        favoriteVenues.value = favoriteVenues.value
    }
}
