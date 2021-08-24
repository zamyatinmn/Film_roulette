package com.geekbrains.filmroulette.viewModel

import com.geekbrains.filmroulette.model.CurrentMovie
import com.geekbrains.filmroulette.model.MovieResult

sealed class AppState {
    data class SuccessNovelty(val novelty: MutableList<MovieResult>) : AppState()
    data class SuccessPopular(val popular: MutableList<MovieResult>) : AppState()
    data class SuccessThriller(val thriller: MutableList<MovieResult>) : AppState()
    data class SuccessComedy(val comedy: MutableList<MovieResult>) : AppState()
    data class Success(val result: MutableList<MovieResult>) : AppState()

    data class ServerError(val error: Throwable) : AppState()
    data class LocalError(val error: Throwable) : AppState()
    data class SuccessCurrent(val film: CurrentMovie) : AppState()
    object Loading : AppState()
}
