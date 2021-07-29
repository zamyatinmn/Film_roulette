package com.geekbrains.filmroulette.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.filmroulette.model.IRepository
import com.geekbrains.filmroulette.model.Repository
import java.util.*

class MainViewModel(
    private val liveDataObserver: MutableLiveData<AppState> = MutableLiveData(),
    val repository: IRepository = Repository()
) : ViewModel() {
    fun getLiveData() = liveDataObserver

    fun getFilmData() = getDataFromLocal()

    //temporary plug
    private fun getDataFromLocal() {
        val rand = Random()
        val i = rand.nextInt(4)
        Log.d("mylogs", i.toString())
        Log.d("mylogs", "i.toString()")
        when (i) {
            0 -> liveDataObserver.value = AppState.Loading
            1 -> liveDataObserver.value = AppState.Success(repository.getFilmFromLocalStorage())
            2 -> liveDataObserver.value =
                AppState.ServerError(Throwable("something wrong with server"))
            3 -> liveDataObserver.value =
                AppState.LocalError(Throwable("something wrong with local"))
        }
    }
}