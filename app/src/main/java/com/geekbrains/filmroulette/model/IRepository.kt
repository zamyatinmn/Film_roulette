package com.geekbrains.filmroulette.model

interface IRepository {
    fun getFilmFromServer(): Film
    fun getFilmFromLocalStorage(): Film
    fun getNovelty(): MutableList<Film>
    fun getPopular(): MutableList<Film>
    fun getThriller(): MutableList<Film>
    fun getComedy(): MutableList<Film>
}