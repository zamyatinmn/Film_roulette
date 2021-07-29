package com.geekbrains.filmroulette.model

class Repository: IRepository{
    override fun getFilmFromServer(): Film {
        return Film()
    }

    override fun getFilmFromLocalStorage(): Film {
        return Film()
    }
}