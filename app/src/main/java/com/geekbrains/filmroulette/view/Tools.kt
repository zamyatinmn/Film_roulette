package com.geekbrains.filmroulette.view

import android.view.View
import com.geekbrains.filmroulette.model.MovieResult
import com.geekbrains.filmroulette.room.FilmEntity

//Немного побаловался с вновь узнанными фичами, удобные штуки
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.setVisible(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

operator fun View.unaryPlus() {
    if (this.visibility == View.GONE) this.visibility = View.INVISIBLE
    if (this.visibility == View.INVISIBLE) this.visibility = View.VISIBLE
}

operator fun View.unaryMinus() {
    if (this.visibility == View.VISIBLE) this.visibility = View.INVISIBLE
    if (this.visibility == View.INVISIBLE) this.visibility = View.GONE
}

fun convertFilmEntityToMovieResult(entity: List<FilmEntity>): MutableList<MovieResult> {
    return entity.map {
        MovieResult(
            it.adult, it.backdrop_path, it.genre_ids, it.id, it.original_language,
            it.original_title, it.overview, it.popularity, it.poster_path, it.release_date,
            it.title, it.video, it.vote_average, it.vote_count, it.like
        )
    }.toMutableList()
}

fun convertMovieResultToEntity(film: MovieResult): FilmEntity {
    return FilmEntity(
        film.adult, film.backdrop_path, film.genre_ids, film.id, film.original_language,
        film.original_title, film.overview, film.popularity, film.poster_path, film.release_date,
        film.title, film.video, film.vote_average, film.vote_count, film.like
    )
}