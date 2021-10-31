package com.geekbrains.filmroulette.model

import com.geekbrains.filmroulette.*
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


/**
 * Created by Maxim Zamyatin on 07.08.2021
 */

class ApiRepository : IRepository {

    val adapter = RxJava3CallAdapterFactory.create()

    private val api = Retrofit.Builder()
        .baseUrl(IMDB_URL)
        .addCallAdapterFactory(adapter)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build().create(FilmAPI::class.java)

    override fun getDataNovelty(language: String) : Single<Results> {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return api.getNovelty(
            API_KEY_VALUE,
            App.isAdultMode,
            SORT_BY_RELEASE_DATE,
            language,
            "$currentYear"
        )
    }

    override fun getDataPopular(language: String): Single<Results> {
        return api.getPopular(API_KEY_VALUE, App.isAdultMode, SORT_BY_POPULARITY, language)
    }

    override fun getDataThriller(language: String): Single<Results> {
        return api.getByGenre(API_KEY_VALUE, App.isAdultMode, language, GENRE_THRILLER)
    }

    override fun getDataComedy(language: String): Single<Results> {
        return api.getByGenre(API_KEY_VALUE, App.isAdultMode, language, GENRE_COMEDY)
    }

    override fun getDataFilm(movieID: Long, language: String): Single<CurrentMovie> {
       return api.getFilmData(movieID, API_KEY_VALUE, language)
    }

    override fun getFilmCredits(movieID: Long, language: String): Single<Credits> {
       return api.getFilmDetails(movieID, API_KEY_VALUE, language)
    }

    override fun getActorDetails(actorId: Long, language: String): Single<Person> {
        return api.getActorDetails(actorId, API_KEY_VALUE, language)
    }
}