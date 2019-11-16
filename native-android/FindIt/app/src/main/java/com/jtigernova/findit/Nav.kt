package com.jtigernova.findit

import android.content.Context
import android.content.Intent
import com.jtigernova.findit.activity.PARAM_VENUE
import com.jtigernova.findit.activity.PARAM_VENUES
import com.jtigernova.findit.activity.VenueDetailActivity
import com.jtigernova.findit.activity.VenuesMapActivity
import com.jtigernova.findit.model.Venue

/**
 * Navigation for app
 */
object Nav {
    /**
     * Send to venue detail screen
     */
    fun venueDetails(context: Context, venue: Venue) {
        Intent(context, VenueDetailActivity::class.java).let {
            it.putExtra(PARAM_VENUE, venue)

            context.startActivity(it)
        }
    }

    /**
     * Send to venues map screen
     */
    fun venuesMap(context: Context, venues: ArrayList<Venue>) {
        Intent(context, VenuesMapActivity::class.java).let {
            it.putParcelableArrayListExtra(PARAM_VENUES, venues)

            context.startActivity(it)
        }
    }
}