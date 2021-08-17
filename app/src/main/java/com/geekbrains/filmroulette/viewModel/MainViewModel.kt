package com.geekbrains.filmroulette.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.filmroulette.model.ApiRepository
import com.geekbrains.filmroulette.model.IRepository
import com.geekbrains.filmroulette.model.Results
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val liveDataObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IRepository = ApiRepository()
) : ViewModel() {
    fun getLiveData() = liveDataObserver

    fun getFilmData(language: String) {
        liveDataObserver.postValue(AppState.Loading)
        repository.getDataNovelty(language, callbackNovelty)
        repository.getDataPopular(language, callbackPopular)
        repository.getDataThriller(language, callbackThriller)
        repository.getDataComedy(language, callbackComedy)
    }

    private val callbackNovelty = object : Callback<Results> {
        override fun onResponse(call: Call<Results>, response: Response<Results>) {
            val serverResponse: Results? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                liveDataObserver.postValue(
                    AppState.SuccessNovelty(serverResponse.results)
                )
            } else {
                liveDataObserver.postValue(AppState.ServerError(Throwable("что-то не так..")))
            }
        }

        override fun onFailure(call: Call<Results>, t: Throwable) {
            liveDataObserver.postValue(AppState.ServerError(t))
        }
    }

    private val callbackPopular = object : Callback<Results> {
        override fun onResponse(call: Call<Results>, response: Response<Results>) {
            val serverResponse: Results? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                liveDataObserver.postValue(
                    AppState.SuccessPopular(serverResponse.results)
                )
            } else {
                liveDataObserver.postValue(AppState.ServerError(Throwable("что-то не так..")))
            }
        }

        override fun onFailure(call: Call<Results>, t: Throwable) {
            liveDataObserver.postValue(AppState.ServerError(t))
        }
    }

    private val callbackThriller = object : Callback<Results> {
        override fun onResponse(call: Call<Results>, response: Response<Results>) {
            val serverResponse: Results? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                liveDataObserver.postValue(
                    AppState.SuccessThriller(serverResponse.results)
                )
            } else {
                liveDataObserver.postValue(AppState.ServerError(Throwable("что-то не так..")))
            }
        }

        override fun onFailure(call: Call<Results>, t: Throwable) {
            liveDataObserver.postValue(AppState.ServerError(t))
        }
    }

    private val callbackComedy = object : Callback<Results> {
        override fun onResponse(call: Call<Results>, response: Response<Results>) {
            val serverResponse: Results? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                liveDataObserver.postValue(
                    AppState.SuccessComedy(serverResponse.results)
                )
            } else {
                liveDataObserver.postValue(AppState.ServerError(Throwable("что-то не так..")))
            }
        }

        override fun onFailure(call: Call<Results>, t: Throwable) {
            liveDataObserver.postValue(AppState.ServerError(t))
        }
    }
}