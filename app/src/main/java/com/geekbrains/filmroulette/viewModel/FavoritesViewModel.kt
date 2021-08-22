package com.geekbrains.filmroulette.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.filmroulette.App
import com.geekbrains.filmroulette.model.*
import com.geekbrains.filmroulette.view.convertFilmEntityToMovieResult


/**
 * Created by Maxim Zamyatin on 18.08.2021
 */


class FavoritesViewModel(
    private val liveDataObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IRepository = ApiRepository(),
    private val favoritesRepository: ILocalRepository = LocalRepository(App.getDao())
) : ViewModel() {


    fun getLiveData() = liveDataObserver

    fun saveFilmToDB(film: MovieResult) {
        favoritesRepository.saveEntity(film)
    }

    fun deleteFilmFromDB(film: MovieResult) {
        favoritesRepository.deleteEntity(film)
    }

    fun getData(): MutableList<MovieResult> {
        return favoritesRepository.getAllFavorites()
    }
}