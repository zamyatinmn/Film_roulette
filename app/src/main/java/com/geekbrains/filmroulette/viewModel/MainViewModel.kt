package com.geekbrains.filmroulette.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.filmroulette.App
import com.geekbrains.filmroulette.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val liveDataObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IRepository = ApiRepository(),
    private val favoritesRepository: ILocalRepository = LocalRepository(App.getDao())
) : ViewModel() {

    private var favoritesFilms = mutableListOf<MovieResult>()

    fun getLiveData() = liveDataObserver

    fun saveFilmToDB(film: MovieResult) {
        favoritesRepository.saveEntity(film)
    }

    fun deleteFilmFromDB(film: MovieResult) {
        favoritesRepository.deleteEntity(film)
    }

    fun getFilmData(language: String) {
        liveDataObserver.postValue(AppState.Loading)
        favoritesRepository.getAllFavorites(object : CallbackDB {
            override fun onResponse(result: MutableList<MovieResult>) {
                favoritesFilms = result
            }
        })
        repository.getDataNovelty(language, callbackNovelty)
        repository.getDataPopular(language, callbackPopular)
        repository.getDataThriller(language, callbackThriller)
        repository.getDataComedy(language, callbackComedy)
    }

    private val callbackNovelty = object : Callback<Results> {
        override fun onResponse(call: Call<Results>, response: Response<Results>) {
            val serverResponse: Results? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                deleteFavoritesFromServerResponse(serverResponse)
                liveDataObserver.value = AppState.SuccessNovelty(serverResponse.results)
            } else {
                liveDataObserver.postValue(AppState.ServerError(Throwable("что-то не так..")))
            }
        }

        override fun onFailure(call: Call<Results>, t: Throwable) {
            liveDataObserver.postValue(AppState.ServerError(t))
        }
    }

    private fun deleteFavoritesFromServerResponse(serverResponse: Results) {
        val temp = mutableListOf<MovieResult>()
        for (favoriteFilm in favoritesFilms) {
            for (serverFilm in serverResponse.results) {
                if (favoriteFilm.id == serverFilm.id) {
                    temp.add(serverFilm)
                }
            }
        }
        for (film in temp) {
            serverResponse.results.remove(film)
        }
    }

    private fun markFavoritesInServerResponse(serverResponse: Results) {
        for (favoriteFilm in favoritesFilms) {
            for (serverFilm in serverResponse.results) {
                if (favoriteFilm.id == serverFilm.id) {
                    serverFilm.like = true
                }
            }
        }
    }

    private val callbackPopular = object : Callback<Results> {
        override fun onResponse(call: Call<Results>, response: Response<Results>) {
            val serverResponse: Results? = response.body()
            if (response.isSuccessful && serverResponse != null) {
                deleteFavoritesFromServerResponse(serverResponse)
                liveDataObserver.value = AppState.SuccessPopular(serverResponse.results)
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
                deleteFavoritesFromServerResponse(serverResponse)
                liveDataObserver.value = AppState.SuccessThriller(serverResponse.results)
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
                deleteFavoritesFromServerResponse(serverResponse)
                liveDataObserver.value = AppState.SuccessComedy(serverResponse.results)
            } else {
                liveDataObserver.postValue(AppState.ServerError(Throwable("что-то не так..")))
            }
        }

        override fun onFailure(call: Call<Results>, t: Throwable) {
            liveDataObserver.postValue(AppState.ServerError(t))
        }
    }
}