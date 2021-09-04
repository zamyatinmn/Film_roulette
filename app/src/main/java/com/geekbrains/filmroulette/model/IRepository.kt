package com.geekbrains.filmroulette.model

import retrofit2.Callback

interface IRepository {
    fun getDataFilm(movieID: Long, language: String, callback: Callback<CurrentMovie>)
    fun getFilmCredits(movieID: Long, language: String, callback: Callback<Credits>)
    fun getDataNovelty(language: String, callback: Callback<Results>)
    fun getDataPopular(language: String, callback: Callback<Results>)
    fun getDataThriller(language: String, callback: Callback<Results>)
    fun getDataComedy(language: String, callback: Callback<Results>)
    fun getActorDetails(actorId: Long, language: String, callback: Callback<Person>)
}