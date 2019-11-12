package com.jtigernova.findit

import android.content.Context
import android.content.Intent
import com.jtigernova.findit.activity.VenueDetailActivity

object Nav {
    fun venueDetails(context: Context) {
        context.startActivity(Intent(context, VenueDetailActivity::class.java))
    }
}