package com.parthdesai.theweatherandnews.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHandler
@Inject
constructor(
    application: Application
) {

    private var networkCallback: ConnectivityManager.NetworkCallback

    private var request: NetworkRequest

    private var manager: ConnectivityManager

    init {
        networkCallback = NetworkCallbackImpl()
        request = NetworkRequest.Builder().build()
        manager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun onActiveInternet() {
        manager.registerNetworkCallback(request, networkCallback)
    }

    fun onInactive() {
        manager.unregisterNetworkCallback(networkCallback)
    }

    class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d("onAvailable: ", "Network is connected")
            Constants.isNetworkConnected = true
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d("onLost: ", "Network Disconnection")
            Constants.isNetworkConnected = false
        }
    }

}