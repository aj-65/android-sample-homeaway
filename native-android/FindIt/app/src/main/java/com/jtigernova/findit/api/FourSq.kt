package com.jtigernova.findit.api

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.jtigernova.findit.model.Venue

/**
 * FourSquare API
 *
 * @param requester Handler for requests
 */
class FourSq(private val requester: IRequester) {

    /**
     * Searches for places given search text
     *
     * @param search Search text
     * @param result Result handler
     */
    fun getPlaces(search: String, result: (success: Boolean, data: Array<Venue>) -> Unit) {
        var url = "https://api.foursquare.com/v2/venues/search"

        //build the request
        url += "?client_id=" + "1T2CTNVBZPZ00LYVP52B3YP4YYL3BCWWIATXUHVMHOCK0UJL"
        url += "&client_secret=" + "UUGMKAK3FXMNP0NQX50NJC145O0TB5GHZAVIQ4UVIO5PT5X3"
        url += "&near=" + "Austin,+TX"
        url += "&query=$search"
        url += "&limit=$LIMIT_COUNT"
        url += "&v=20180401"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    //get venues json
                    val venueJson = response.getJSONObject("response")
                            .getJSONArray("venues").toString()

                    //parse
                    val res = Gson().fromJson(venueJson, Array<Venue>::class.java)

                    //Volley always returns on UI thread, so we are safe
                    result(true, res)
                },
                Response.ErrorListener {
                    result(false, arrayOf())
                })

        //do request
        requester.doRequest(request)
    }

    companion object {
        /**
         * List limit
         */
        const val LIMIT_COUNT = 15
    }
}