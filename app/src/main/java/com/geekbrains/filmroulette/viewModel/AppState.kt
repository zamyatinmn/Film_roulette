package com.geekbrains.filmroulette.viewModel

import com.geekbrains.filmroulette.model.Cast
import com.geekbrains.filmroulette.model.CurrentMovie
import com.geekbrains.filmroulette.model.MovieResult
import com.geekbrains.filmroulette.model.Person

sealed class AppState {
    data class SuccessNovelty(val novelty: MutableList<MovieResult>) : AppState()
    data class SuccessPopular(val popular: MutableList<MovieResult>) : AppState()
    data class SuccessThriller(val thriller: MutableList<MovieResult>) : AppState()
    data class SuccessComedy(val comedy: MutableList<MovieResult>) : AppState()
    data class Success(val result: MutableList<MovieResult>) : AppState()
    data class SuccessCast(val result: List<Cast>) : AppState()
    data class ServerError(val error: Throwable) : AppState()
    data class LocalError(val error: Throwable) : AppState()
    data class SuccessCurrent(val film: CurrentMovie) : AppState()
    data class OpenMap(val person: Person) : AppState()
    object Loading : AppState()
}
