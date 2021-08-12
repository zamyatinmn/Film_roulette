package com.geekbrains.filmroulette.viewModel

import com.geekbrains.filmroulette.model.MovieResult

sealed class AppState {
    data class Success(
        val novelty: MutableList<MovieResult>, val popular: MutableList<MovieResult>,
        val thriller: MutableList<MovieResult>, val comedy: MutableList<MovieResult>
    ) : AppState()

    data class ServerError(val error: Throwable) : AppState()
    data class LocalError(val error: Throwable) : AppState()
    object Loading : AppState()
}
