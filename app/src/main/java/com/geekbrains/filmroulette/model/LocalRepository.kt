package com.geekbrains.filmroulette.model

import com.geekbrains.filmroulette.room.FilmDao
import com.geekbrains.filmroulette.view.convertFilmEntityToMovieResult
import com.geekbrains.filmroulette.view.convertMovieResultToEntity


/**
 * Created by Maxim Zamyatin on 18.08.2021
 */


class LocalRepository(private val localDataSource: FilmDao) : ILocalRepository {

    override fun getAllFavorites(): MutableList<MovieResult> {
        return convertFilmEntityToMovieResult(localDataSource.all())
    }

    override fun saveEntity(film: MovieResult) {
        localDataSource.insert(convertMovieResultToEntity(film))
    }

    override fun deleteEntity(film: MovieResult) {
        localDataSource.delete(convertMovieResultToEntity(film))
    }
}