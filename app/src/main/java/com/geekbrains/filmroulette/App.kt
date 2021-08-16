package com.geekbrains.filmroulette

import android.app.Application

/**
 * Created by Maxim Zamyatin on 16.08.2021
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val networkState = NetworkState()
        networkState.enable(this)
    }
}