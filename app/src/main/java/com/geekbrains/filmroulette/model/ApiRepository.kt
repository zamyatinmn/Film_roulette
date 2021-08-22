package com.geekbrains.filmroulette.model

import com.geekbrains.filmroulette.*
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


/**
 * Created by Maxim Zamyatin on 07.08.2021
 */

class ApiRepository : IRepository {

    private val api = Retrofit.Builder()
        .baseUrl(IMDB_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build().create(FilmAPI::class.java)

    override fun getDataNovelty(language: String, callback: Callback<Results>) {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        api.getNovelty(BuildConfig.API_KEY, App.isAdultMode, SORT_BY_RELEASE_DATE, language, "$currentYear")
            .enqueue(callback)
    }

    override fun getDataPopular(language: String, callback: Callback<Results>) {
        api.getPopular(BuildConfig.API_KEY, App.isAdultMode, SORT_BY_POPULARITY, language)
            .enqueue(callback)
    }

    override fun getDataThriller(language: String, callback: Callback<Results>) {
        api.getByGenre(BuildConfig.API_KEY, App.isAdultMode, language, GENRE_THRILLER)
            .enqueue(callback)
    }

    override fun getDataComedy(language: String, callback: Callback<Results>) {
        api.getByGenre(BuildConfig.API_KEY, App.isAdultMode, language, GENRE_COMEDY)
            .enqueue(callback)
    }

    override fun getDataFilm(movieID: Long, language: String, callback: Callback<CurrentMovie>) {
        api.getFilmData(movieID, BuildConfig.API_KEY, language)
            .enqueue(callback)
    }
}