package com.geekbrains.filmroulette.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.filmroulette.model.ApiRepository
import com.geekbrains.filmroulette.model.IRepository

class MainViewModel(
    private val liveDataObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IRepository = ApiRepository()
) : ViewModel() {
    fun getLiveData() = liveDataObserver

    fun getFilmData() = getDataFromLocal()
    fun getFilm(id: Long) = repository.getFilmFromServer(id)

    //temporary plug
    private fun getDataFromLocal() {
        when (1) {
            0 -> liveDataObserver.value = AppState.Loading
            1 -> repository.apply {
                liveDataObserver.value = AppState.Success(
                    getNovelty(), getPopular(), getThriller(), getComedy()
                )
            }
            2 -> liveDataObserver.value =
                AppState.ServerError(Throwable("something wrong with server"))
            3 -> liveDataObserver.value =
                AppState.LocalError(Throwable("something wrong with local"))
        }
    }
}