package com.jtigernova.findit.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.jtigernova.findit.api.FourSq
import com.jtigernova.findit.api.IRequester
import com.jtigernova.findit.api.NetworkSingleton

/**
 * Base activity for app
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), IRequester {

    private lateinit var networkSingleton: NetworkSingleton

    /**
     * API for FourSquare
     */
    protected lateinit var mFourSq: FourSq

    private val tag = javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //use the same networking for each activity
        networkSingleton = NetworkSingleton.getInstance(this@BaseActivity)
        mFourSq = FourSq(this@BaseActivity)
    }

    /***
     * Queues a request for execution
     */
    override fun <T> doRequest(req: Request<T>) {
        networkSingleton.addToRequestQueue(req, tag)
    }

    /**
     * Cancels all requests queued by this activity
     */
    protected fun cancelRequests() {
        Log.d(javaClass.simpleName, "Cancelling requests for: $tag")

        networkSingleton.cancel(tag)
    }

    override fun onStop() {
        super.onStop()

        //make sure we stop network requests
        cancelRequests()
    }

    override fun onDestroy() {
        super.onDestroy()

        //make sure we stop network requests
        cancelRequests()
    }
}