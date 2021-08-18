package com.geekbrains.filmroulette.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.filmroulette.model.ApiRepository
import com.geekbrains.filmroulette.model.CurrentMovie
import com.geekbrains.filmroulette.model.IRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Maxim Zamyatin on 17.08.2021
 */


class CurrentViewModel(
    private val liveDataObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IRepository = ApiRepository()
) : ViewModel() {

    fun getLiveData() = liveDataObserver

    fun getFilm(id: Long, language: String) {
        liveDataObserver.postValue(AppState.Loading)
        repository.getDataFilm(id, language, callbackMovie)
    }

    private val callbackMovie = object : Callback<CurrentMovie> {
        override fun onResponse(call: Call<CurrentMovie>, response: Response<CurrentMovie>) {
            val serverResponse: CurrentMovie? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                liveDataObserver.postValue(AppState.SuccessCurrent(serverResponse))
            } else {
                liveDataObserver.postValue(AppState.ServerError(Throwable("что-то не так..")))
            }
        }

        override fun onFailure(call: Call<CurrentMovie>, t: Throwable) {
            liveDataObserver.postValue(AppState.ServerError(t))
        }
    }
}