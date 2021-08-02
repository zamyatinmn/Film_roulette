package com.geekbrains.filmroulette.view

import com.geekbrains.filmroulette.model.Film

interface OnItemClickListener {
    fun onClick(film: Film)
}