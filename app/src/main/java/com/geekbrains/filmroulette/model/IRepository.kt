package com.geekbrains.filmroulette.model

import com.geekbrains.filmroulette.viewModel.AppState

interface IRepository {
    fun getFilmFromServer(movieID: Long): CurrentMovie
    fun getFilmFromLocalStorage(): Film {
        return Film()
    }
    fun getNovelty(): MutableList<MovieResult>
    fun getPopular(): MutableList<MovieResult>
    fun getThriller(): MutableList<MovieResult>
    fun getComedy(): MutableList<MovieResult>
}