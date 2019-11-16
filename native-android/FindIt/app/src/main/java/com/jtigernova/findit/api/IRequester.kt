package com.jtigernova.findit.api

import com.android.volley.Request

/**
 * Requester for doing requests
 */
interface IRequester {
    /**
     * Does a request
     *
     * @param req Request to perform
     */
    fun <T> doRequest(req: Request<T>)
}