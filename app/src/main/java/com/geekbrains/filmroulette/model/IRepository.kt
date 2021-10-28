package com.geekbrains.filmroulette.model

import io.reactivex.rxjava3.core.Single

interface IRepository {
    fun getDataFilm(movieID: Long, language: String): Single<CurrentMovie>
    fun getFilmCredits(movieID: Long, language: String): Single<Credits>
    fun getDataNovelty(language: String): Single<Results>
    fun getDataPopular(language: String): Single<Results>
    fun getDataThriller(language: String): Single<Results>
    fun getDataComedy(language: String): Single<Results>
    fun getActorDetails(actorId: Long, language: String): Single<Person>
}