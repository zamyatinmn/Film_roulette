package com.geekbrains.filmroulette.model


/**
 * Created by Maxim Zamyatin on 18.08.2021
 */


interface ILocalRepository {
    fun getAllFavorites(callback: CallbackDB)
    fun saveEntity(film: MovieResult)
    fun deleteEntity(film: MovieResult)
}