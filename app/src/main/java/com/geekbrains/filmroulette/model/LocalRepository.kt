package com.geekbrains.filmroulette.model

import com.geekbrains.filmroulette.room.FilmDao
import com.geekbrains.filmroulette.view.convertFilmEntityToMovieResult
import com.geekbrains.filmroulette.view.convertMovieResultToEntity


/**
 * Created by Maxim Zamyatin on 18.08.2021
 */


class LocalRepository(private val localDataSource: FilmDao) : ILocalRepository {

    override fun getAllFavorites(callback: CallbackDB) {
        Thread {
            val temp = convertFilmEntityToMovieResult(localDataSource.all())
            callback.onResponse(temp)
        }.start()
    }

    override fun saveEntity(film: MovieResult) {
        Thread {
            localDataSource.insert(convertMovieResultToEntity(film))
        }.start()
    }

    override fun deleteEntity(film: MovieResult) {
        Thread {
            localDataSource.delete(convertMovieResultToEntity(film))
        }.start()
    }
}