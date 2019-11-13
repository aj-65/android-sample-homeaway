package com.jtigernova.findit.api

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.jtigernova.findit.data.Venue

class FourSq(private val requester: IRequester) {

    fun test(result: (success: Boolean, data: Venue?) -> Unit) {
        val url = "https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    result(true, null)
                },
                Response.ErrorListener { result(false, null) })

        requester.doRequest(request)
    }
}