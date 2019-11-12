package com.jtigernova.findit

import android.content.Context
import android.content.Intent
import com.jtigernova.findit.activity.VenueDetailActivity
import com.jtigernova.findit.data.Venue

object Nav {
    fun venueDetails(context: Context, venue: Venue) {
        Intent(context, VenueDetailActivity::class.java).let {
            it.putExtra(Venue::class.java.name, venue)

            context.startActivity(it)
        }
    }
}