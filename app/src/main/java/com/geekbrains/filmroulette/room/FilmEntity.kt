package com.geekbrains.filmroulette.room

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Created by Maxim Zamyatin on 18.08.2021
 */

@Entity
data class FilmEntity(
    val adult: Boolean,
    val backdrop_path: String,
//    @SerializedName("Long")
    val genre_ids: List<Long>,
    @PrimaryKey
    val id: Long,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Long,
    var like: Boolean,
)