package com.jtigernova.findit.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Networking singleton
 */
class NetworkSingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: NetworkSingleton? = null

        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: NetworkSingleton(context).also {
                        INSTANCE = it
                    }
                }
    }

    val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    /**
     * Queues up a request
     *
     * @param req Request to queue
     * @param tag Tag to assign to request
     */
    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        req.tag = tag
        requestQueue.add(req)
    }

    /**
     * Cancels requests by tag
     *
     * @param Tag to filter by
     */
    fun cancel(tag: String) {
        requestQueue.cancelAll(tag)
    }
}
