package com.geekbrains.filmroulette.model

interface IRepository {
    fun getFilmFromServer(): Film
    fun getFilmFromLocalStorage(): Film

}