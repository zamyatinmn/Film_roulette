package com.geekbrains.filmroulette

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.geekbrains.filmroulette.model.ApiRepository
import com.geekbrains.filmroulette.model.LocalRepository
import com.geekbrains.filmroulette.model.Results
import com.geekbrains.filmroulette.viewModel.AppState
import com.geekbrains.filmroulette.viewModel.MainViewModel
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class ViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repo: ApiRepository

    @Mock
    private lateinit var localRepo: LocalRepository

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        mainViewModel = MainViewModel(repo, localRepo)
    }

    @Test
    fun search_Test() {
        Mockito.`when`(repo.getDataPopular(LANGUAGE)).thenReturn(
            Single.just(Results(1, mutableListOf(), 1, 1))
        )
        Mockito.`when`(repo.getDataComedy(LANGUAGE)).thenReturn(
            Single.just(Results(1, mutableListOf(), 1, 1))
        )
        Mockito.`when`(repo.getDataNovelty(LANGUAGE)).thenReturn(
            Single.just(Results(1, mutableListOf(), 1, 1))
        )
        Mockito.`when`(repo.getDataThriller(LANGUAGE)).thenReturn(
            Single.just(Results(1, mutableListOf(), 1, 1))
        )

        mainViewModel.getFilmData(LANGUAGE)
        verify(repo, times(1)).getDataPopular(LANGUAGE)
        verify(repo, times(1)).getDataComedy(LANGUAGE)
        verify(repo, times(1)).getDataNovelty(LANGUAGE)
        verify(repo, times(1)).getDataThriller(LANGUAGE)
    }

    @Test
    fun liveData_TestReturnValueIsNotNull() {
        val observer = Observer<AppState> {}
        val liveData = mainViewModel.getLiveData()

        Mockito.`when`(repo.getDataPopular(LANGUAGE)).thenReturn(
            Single.just(Results(1, mutableListOf(), 1, 1))
        )
        Mockito.`when`(repo.getDataComedy(LANGUAGE)).thenReturn(
            Single.just(Results(1, mutableListOf(), 1, 1))
        )
        Mockito.`when`(repo.getDataNovelty(LANGUAGE)).thenReturn(
            Single.just(Results(1, mutableListOf(), 1, 1))
        )
        Mockito.`when`(repo.getDataThriller(LANGUAGE)).thenReturn(
            Single.just(Results(1, mutableListOf(), 1, 1))
        )

        Mockito.`when`(localRepo.getAllFavorites(any())).then { }

        try {
            liveData.observeForever(observer)
            mainViewModel.getFilmData(LANGUAGE)
            assertNotNull(liveData.value)
        } finally {
            liveData.removeObserver(observer)
        }
    }

    companion object {
        const val LANGUAGE = ""
    }
}