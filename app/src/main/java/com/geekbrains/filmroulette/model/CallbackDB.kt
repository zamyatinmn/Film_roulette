package com.geekbrains.filmroulette.model


/**
 * Created by Maxim Zamyatin on 24.08.2021
 */


interface CallbackDB {
    fun onResponse(result: MutableList<MovieResult>)
}