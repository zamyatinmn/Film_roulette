package com.geekbrains.filmroulette.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.filmroulette.model.IRepository
import com.geekbrains.filmroulette.model.Repository
import java.util.*

class MainViewModel(
    private val liveDataObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IRepository = Repository()
) : ViewModel() {
    fun getLiveData() = liveDataObserver

    fun getFilmData() = getDataFromLocal()

    //temporary plug
    private fun getDataFromLocal() {
        val rand = Random()
        when (rand.nextInt(4)) {
            0 -> liveDataObserver.value = AppState.Loading
            1 -> liveDataObserver.value = AppState.Success(
                repository.getNovelty(),
                repository.getPopular(),
                repository.getThriller(),
                repository.getComedy()
            )
            2 -> liveDataObserver.value =
                AppState.ServerError(Throwable("something wrong with server"))
            3 -> liveDataObserver.value =
                AppState.LocalError(Throwable("something wrong with local"))
        }
    }
}