package com.geekbrains.filmroulette

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.widget.Toast


/**
 * Created by Maxim Zamyatin on 16.08.2021
 */

const val CELLULAR_CONNECTION = 124

class NetworkState : NetworkCallback() {
    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    private lateinit var context: Context
    private var networkType = -1

    fun enable(context: Context) {
        this.context = context
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        val newNetworkType = network.toString().toInt()
        if (newNetworkType != networkType) {
            networkType = newNetworkType
            when (networkType) {
                CELLULAR_CONNECTION -> Toast.makeText(
                    context,
                    "Соединение по сотовой сети",
                    Toast.LENGTH_SHORT
                ).show()
                else -> Toast.makeText(context, "Соединение по wi-fi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}