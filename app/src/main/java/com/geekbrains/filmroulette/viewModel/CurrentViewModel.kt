package com.geekbrains.filmroulette.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.filmroulette.App
import com.geekbrains.filmroulette.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by Maxim Zamyatin on 17.08.2021
 */


class CurrentViewModel(
    private val liveDataObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IRepository = ApiRepository(),
    private val localRepository: ILocalRepository = LocalRepository(App.getDao())
) : ViewModel() {

    private var favorites = mutableListOf<MovieResult>()

    fun getLiveData() = liveDataObserver

    fun getFilm(id: Long, language: String) {
        liveDataObserver.postValue(AppState.Loading)
        localRepository.getAllFavorites(object : CallbackDB {
            override fun onResponse(result: MutableList<MovieResult>) {
                favorites = result
            }

        })
        repository.getDataFilm(id, language, callbackMovie)
    }

    private val callbackMovie = object : Callback<CurrentMovie> {
        override fun onResponse(call: Call<CurrentMovie>, response: Response<CurrentMovie>) {
            val serverResponse: CurrentMovie? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                for (film in favorites) {
                    if (serverResponse.id == film.id) {
                        serverResponse.like = true
                    }
                }
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