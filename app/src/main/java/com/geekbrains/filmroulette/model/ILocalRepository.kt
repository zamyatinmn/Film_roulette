package com.geekbrains.filmroulette.model


/**
 * Created by Maxim Zamyatin on 18.08.2021
 */


interface ILocalRepository {
    fun getAllFavorites(): MutableList<MovieResult>
    fun saveEntity(film: MovieResult)
    fun deleteEntity(film: MovieResult)
}