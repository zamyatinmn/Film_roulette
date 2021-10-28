package com.geekbrains.filmroulette.viewModel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.filmroulette.App
import com.geekbrains.filmroulette.model.*
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val repository: IRepository = ApiRepository(),
    private val favoritesRepository: ILocalRepository = LocalRepository(App.getDao())
) : ViewModel() {

    private var favoritesFilms = mutableListOf<MovieResult>()

    private val subjectLoading = BehaviorSubject.create<Unit>()
    private val subjectRequest = BehaviorSubject.create<String>()

    private val observableResponse = subjectRequest
        .doOnNext { subjectLoading.onNext(Unit) }
        .flatMap { language ->
            Observable.merge(
                listOf(
                    repository.getDataNovelty(language).map { response ->
                        deleteFavoritesFromServerResponse(response)
                        AppState.SuccessNovelty(response.results)
                    }.toObservable(),
                    repository.getDataPopular(language).map { response ->
                        deleteFavoritesFromServerResponse(response)
                        AppState.SuccessPopular(response.results)
                    }.toObservable(),
                    repository.getDataThriller(language).map { response ->
                        deleteFavoritesFromServerResponse(response)
                        AppState.SuccessThriller(response.results)
                    }.toObservable(),
                    repository.getDataComedy(language).map { response ->
                        deleteFavoritesFromServerResponse(response)
                        AppState.SuccessComedy(response.results)
                    }.toObservable()
                )
            )
        }

    private val screenState = Observable.merge(listOf(
        subjectLoading.map { AppState.Loading },
        observableResponse.doOnError { AppState.ServerError(it) }
    ))

    private val liveDataObserver =
        LiveDataReactiveStreams.fromPublisher(screenState.toFlowable(BackpressureStrategy.LATEST))

    fun getLiveData() = liveDataObserver

    fun saveFilmToDB(film: MovieResult) {
        favoritesRepository.saveEntity(film)
    }

    fun deleteFilmFromDB(film: MovieResult) {
        favoritesRepository.deleteEntity(film)
    }

    fun getFilmData(language: String) {
        subjectRequest.onNext(language)
        favoritesRepository.getAllFavorites(object : CallbackDB {
            override fun onResponse(result: MutableList<MovieResult>) {
                favoritesFilms = result
            }
        })
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
}