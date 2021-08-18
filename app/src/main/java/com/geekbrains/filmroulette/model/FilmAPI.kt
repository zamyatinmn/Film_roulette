package com.geekbrains.filmroulette.model

import com.geekbrains.filmroulette.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by Maxim Zamyatin on 17.08.2021
 */

interface FilmAPI {
    @GET(DISCOVER_ENDPOINT)
    fun getNovelty(
        @Query(API_KEY_NAME) token:String,
        @Query(SORT_KEY) sort:String,
        @Query(LANGUAGE_KEY) language: String,
        @Query(YEAR_KEY) year: String
    ) : Call<Results>

    @GET(DISCOVER_ENDPOINT)
    fun getPopular(
        @Query(API_KEY_NAME) token:String,
        @Query(SORT_KEY) sort:String,
        @Query(LANGUAGE_KEY) language: String
    ) : Call<Results>

    @GET(DISCOVER_ENDPOINT)
    fun getByGenre(
        @Query(API_KEY_NAME) token:String,
        @Query(LANGUAGE_KEY) language: String,
        @Query(GENRE_KEY) genreId: Int
    ) : Call<Results>

    @GET("$DETAILS_ENDPOINT{id}")
    fun getFilmData(
        @Path("id") movieId: Long,
        @Query(API_KEY_NAME) token:String,
        @Query(LANGUAGE_KEY) language: String,
    ) : Call<CurrentMovie>
}