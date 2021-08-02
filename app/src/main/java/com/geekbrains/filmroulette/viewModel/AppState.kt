package com.geekbrains.filmroulette.viewModel

import com.geekbrains.filmroulette.model.Film

sealed class AppState {
    data class Success(
        val novelty: MutableList<Film>, val popular: MutableList<Film>,
        val thriller: MutableList<Film>, val comedy: MutableList<Film>
    ) : AppState()

    data class ServerError(val error: Throwable) : AppState()
    data class LocalError(val error: Throwable) : AppState()
    object Loading : AppState()
}
