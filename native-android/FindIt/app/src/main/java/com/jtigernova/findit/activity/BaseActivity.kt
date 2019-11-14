package com.jtigernova.findit.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.jtigernova.findit.api.FourSq
import com.jtigernova.findit.api.IRequester
import com.jtigernova.findit.api.NetworkSingleton

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), IRequester {

    private lateinit var networkSingleton: NetworkSingleton

    protected lateinit var mFourSq: FourSq

    private val tag = javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkSingleton = NetworkSingleton.getInstance(this@BaseActivity)
        mFourSq = FourSq(this@BaseActivity)
    }

    override fun <T> doRequest(req: Request<T>) {
        req.tag = this.javaClass.name

        networkSingleton.addToRequestQueue(req)
    }

    protected fun cancelRequests() {
        Log.d(javaClass.simpleName, "Cancelling requests for: $tag")

        networkSingleton.cancel(tag)
    }

    override fun onStop() {
        super.onStop()

        cancelRequests()
    }

    override fun onDestroy() {
        super.onDestroy()

        cancelRequests()
    }
}