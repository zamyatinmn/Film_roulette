package com.geekbrains.filmroulette.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.filmroulette.model.IRepository
import com.geekbrains.filmroulette.model.Repository

class MainViewModel(
    private val liveDataObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IRepository = Repository()
) : ViewModel() {
    fun getLiveData() = liveDataObserver

    fun getFilmData() = getDataFromLocal()

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