package com.geekbrains.filmroulette.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.filmroulette.R
import com.geekbrains.filmroulette.databinding.FragmentMainBinding
import com.geekbrains.filmroulette.model.MovieResult
import com.geekbrains.filmroulette.view.CurrentFilmFragment.Companion.KEY_FILM
import com.geekbrains.filmroulette.viewModel.AppState
import com.geekbrains.filmroulette.viewModel.MainViewModel

class MainFragment : Fragment() {

    private var _ui: FragmentMainBinding? = null
    private val ui get() = _ui!!
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var noveltyAdapter: FilmsAdapter = FilmsAdapter()
    private var popularAdapter: FilmsAdapter = FilmsAdapter()
    private var thrillerAdapter: FilmsAdapter = FilmsAdapter()
    private var comedyAdapter: FilmsAdapter = FilmsAdapter()

    companion object {
        fun newInstance(): Fragment = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _ui = FragmentMainBinding.inflate(inflater)

        ui.apply {
            initRecyclerView(noveltyRecyclerView, noveltyAdapter)
            initRecyclerView(popularRecyclerView, popularAdapter)
            initRecyclerView(thrillerRecyclerView, thrillerAdapter)
            initRecyclerView(comedyRecyclerView, comedyAdapter)
        }

        setRefreshBehaviour()
        return ui.root
    }

    private fun getData() {
        ui.loading.visible()
        viewModel.getFilmData(getString(R.string.language))
    }

    private fun initRecyclerView(recyclerView: RecyclerView, adapter: FilmsAdapter) {
        recyclerView.setHasFixedSize(true)
        setHasOptionsMenu(true)
        val layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        recyclerView.layoutManager = layoutManager
        adapter.itemClickListener = onFilmClickListener()
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getData()
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setRefreshBehaviour() {
        ui.swipeRefresh.setOnRefreshListener {
            ui.loading.visible()
            getData()
            ui.swipeRefresh.isRefreshing = false
        }
    }

    private fun onFilmClickListener() = object : OnItemClickListener {
        override fun onClick(film: MovieResult) {
            parentFragmentManager.beginTransaction().addToBackStack(this@MainFragment.tag)
                .replace(R.id.container, CurrentFilmFragment.newInstance(Bundle().apply {
                    putLong(KEY_FILM, film.id)
                })).commit()
        }
    }

    override fun onDestroy() {
        _ui = null
        super.onDestroy()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Loading -> ui.loading.visible()
            is AppState.LocalError -> {
                //adsad
            }
            is AppState.ServerError -> {
                //asd}
            }
            is AppState.SuccessNovelty -> {
                ui.loading.gone()
                noveltyAdapter.setFilmData(appState.novelty)
            }
            is AppState.SuccessPopular -> {
                ui.loading.gone()
                popularAdapter.setFilmData(appState.popular)
            }
            is AppState.SuccessThriller -> {
                ui.loading.gone()
                thrillerAdapter.setFilmData(appState.thriller)
            }
            is AppState.SuccessComedy -> {
                ui.loading.gone()
                comedyAdapter.setFilmData(appState.comedy)
            }
        }
    }
}