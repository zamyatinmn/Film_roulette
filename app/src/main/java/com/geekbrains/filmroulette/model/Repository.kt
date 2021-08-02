package com.geekbrains.filmroulette.model

class Repository : IRepository {
    override fun getFilmFromServer(): Film {
        return Film()
    }

    override fun getFilmFromLocalStorage(): Film {
        return Film()
    }

    override fun getNovelty(): MutableList<Film> {
        return getTemporaryData()
    }

    override fun getPopular(): MutableList<Film> {
        return getTemporaryData()
    }

    override fun getThriller(): MutableList<Film> {
        return getTemporaryData()
    }

    override fun getComedy(): MutableList<Film> {
        return getTemporaryData()
    }
}

fun getTemporaryData(): MutableList<Film> {
    return mutableListOf(
        Film("Чужой", 1999, 7f),
        Film("Хищник", 1984, 6.7f),
        Film("Свой", 1995, 8f),
        Film("Травоядный", 1889, 5f),
        Film("Красавчик", 2015, 9f),
        Film("Страшила", 2020, 8f),
        Film("Чужой", 1999, 7f),
        Film("Хищник", 1984, 6.7f),
        Film("Свой", 1995, 8f),
        Film("Травоядный", 1889, 5f),
        Film("Красавчик", 2015, 9f),
        Film("Страшила", 2020, 8f),
        Film("Чужой", 1999, 7f),
        Film("Хищник", 1984, 6.7f),
        Film("Свой", 1995, 8f),
        Film("Травоядный", 1889, 5f),
        Film("Красавчик", 2015, 9f),
        Film("Страшила", 2020, 8f),
        Film("Чужой", 1999, 7f),
        Film("Хищник", 1984, 6.7f),
        Film("Свой", 1995, 8f),
        Film("Травоядный", 1889, 5f),
        Film("Красавчик", 2015, 9f),
        Film("Страшила", 2020, 8f)
    )
}