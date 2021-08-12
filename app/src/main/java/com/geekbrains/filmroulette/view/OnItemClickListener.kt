package com.geekbrains.filmroulette.view

import com.geekbrains.filmroulette.model.MovieResult

interface OnItemClickListener {
    fun onClick(film: MovieResult)
}