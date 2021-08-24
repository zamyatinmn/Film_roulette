package com.geekbrains.filmroulette.view

import com.geekbrains.filmroulette.model.MovieResult


/**
 * Created by Maxim Zamyatin on 18.08.2021
 */


interface OnClickLike {
    fun onClick(film: MovieResult)
}