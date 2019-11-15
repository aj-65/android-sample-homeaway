package com.jtigernova.findit.api

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.jtigernova.findit.model.Venue
import org.json.JSONObject

class FourSq(private val requester: IRequester) {

    fun getPlaces(search: String, result: (success: Boolean, data: Array<Venue>) -> Unit) {
        var url = "https://api.foursquare.com/v2/venues/search"

        val json = JSONObject().apply {
            put("client_id", "1T2CTNVBZPZ00LYVP52B3YP4YYL3BCWWIATXUHVMHOCK0UJL")
            put("client_secret", "UUGMKAK3FXMNP0NQX50NJC145O0TB5GHZAVIQ4UVIO5PT5X3")
            put("near", "Austin, TX")
            put("query", search)
            put("limit", 25)
        }

        url += "?client_id=" + "1T2CTNVBZPZ00LYVP52B3YP4YYL3BCWWIATXUHVMHOCK0UJL"
        url += "&client_secret=" + "UUGMKAK3FXMNP0NQX50NJC145O0TB5GHZAVIQ4UVIO5PT5X3"
        url += "&near=" + "Austin,+TX"
        url += "&query=$search"
        url += "&limit=" + 25
        url += "&v=20180401"

        val request = JsonObjectRequest(Request.Method.GET, url, json,
                Response.Listener { response ->
                    val venueJson = response.getJSONObject("response").getJSONArray("venues").toString()

                    val res = Gson().fromJson(venueJson, Array<Venue>::class.java)

                    result(true, res)
                },
                Response.ErrorListener {
                    val i = it.toString()
                    result(false, arrayOf())
                })

        requester.doRequest(request)
    }
}