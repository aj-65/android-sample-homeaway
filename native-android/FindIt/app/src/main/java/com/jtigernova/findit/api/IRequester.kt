package com.jtigernova.findit.api

import com.android.volley.Request

interface IRequester {
    fun <T> doRequest(req: Request<T>)
}