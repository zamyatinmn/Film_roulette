package com.geekbrains.filmroulette.viewModel

import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.geekbrains.filmroulette.App
import com.geekbrains.filmroulette.model.*
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject


/**
 * Created by Maxim Zamyatin on 17.08.2021
 */


class CurrentViewModel(
    private val repository: IRepository = ApiRepository(),
    private val localRepository: ILocalRepository = LocalRepository(App.getDao())
) : ViewModel() {

    private var favorites = mutableListOf<MovieResult>()

    private val subjectLoading = BehaviorSubject.create<Unit>()
    private val subjectRequest = BehaviorSubject.create<Pair<Long, String>>()
    private val actorRequest = BehaviorSubject.create<Pair<Long, String>>()
    private val creditsRequest = BehaviorSubject.create<Pair<Long, String>>()

    private val observableResponse = subjectRequest
        .doOnNext { subjectLoading.onNext(Unit) }
        .flatMap {
            repository.getDataFilm(it.first, it.second).map { movie ->
                for (film in favorites) {
                    if (movie.id == film.id) {
                        movie.like = true
                    }
                }
                AppState.SuccessCurrent(movie)
            }.toObservable()
        }

    private val observableActor = actorRequest
        .doOnNext { subjectLoading.onNext(Unit) }
        .flatMap {
            repository.getActorDetails(it.first, it.second).map { person ->
                AppState.OpenMap(person)
            }.toObservable()
        }

    private val observableCredits = creditsRequest
        .doOnNext { subjectLoading.onNext(Unit) }
        .flatMap {
            repository.getFilmCredits(it.first, it.second).map { credits ->
                for (cast in credits.cast) {
                    cast.cast_id
                    cast.name
                }
                AppState.SuccessCast(credits.cast)
            }.toObservable()
        }

    private val screenState = Observable.merge(listOf(
        subjectLoading.map { AppState.Loading },
        observableResponse,
        observableActor,
        observableCredits
    ))

    private val liveDataObserver =
        LiveDataReactiveStreams.fromPublisher(screenState.toFlowable(BackpressureStrategy.LATEST))

    fun getLiveData() = liveDataObserver

    fun getCasts(id: Long, language: String) {
        creditsRequest.onNext(Pair(id, language))
    }

    fun getFilm(id: Long, language: String) {
        subjectRequest.onNext(Pair(id, language))
        localRepository.getAllFavorites(object : CallbackDB {
            override fun onResponse(result: MutableList<MovieResult>) {
                favorites = result
            }
        })
    }

    fun getActorPlaceOfBirth(id: Long, language: String) {
        actorRequest.onNext(Pair(id, language))
    }
}