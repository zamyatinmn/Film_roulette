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

    fun getCasts(id: Long, language: String) {
        repository.getFilmCredits(id, language, callbackCredits)
    }

    fun getFilm(id: Long, language: String) {
        liveDataObserver.postValue(AppState.Loading)
        localRepository.getAllFavorites(object : CallbackDB {
            override fun onResponse(result: MutableList<MovieResult>) {
                favorites = result
            }

        })
        repository.getDataFilm(id, language, callbackMovie)
    }

    fun getActorPlaceOfBirth(id: Long, language: String) {
        repository.getActorDetails(id, language, actorCallback)
    }

    private val actorCallback = object : Callback<Person> {
        override fun onResponse(call: Call<Person>, response: Response<Person>) {
            val responsePerson: Person? = response.body()
            if (response.isSuccessful && responsePerson != null) {
                liveDataObserver.postValue(AppState.OpenMap(responsePerson))
            }
        }

        override fun onFailure(call: Call<Person>, t: Throwable) {
            liveDataObserver.postValue(AppState.ServerError(t))
        }
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

    private val callbackCredits = object : Callback<Credits> {
        override fun onResponse(call: Call<Credits>, response: Response<Credits>) {
            val creditsResponse: Credits? = response.body()
            if (response.isSuccessful && creditsResponse != null){
                for (cast in creditsResponse.cast){
                    cast.cast_id
                    cast.name
                }
                liveDataObserver.postValue(AppState.SuccessCast(creditsResponse.cast))
            } else {
                liveDataObserver.postValue(AppState.ServerError(Throwable(response.errorBody().toString())))
            }
        }

        override fun onFailure(call: Call<Credits>, t: Throwable) {
            liveDataObserver.postValue(AppState.ServerError(t))
        }
    }
}