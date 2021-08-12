package com.geekbrains.filmroulette.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.filmroulette.R
import com.geekbrains.filmroulette.databinding.FragmentMainBinding
import com.geekbrains.filmroulette.model.Film
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
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getFilmData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Success -> {
                ui.loading.gone()
                noveltyAdapter.setFilmData(state.novelty)
                popularAdapter.setFilmData(state.popular)
                thrillerAdapter.setFilmData(state.thriller)
                comedyAdapter.setFilmData(state.comedy)
            }
            is AppState.Loading -> ui.loading.visible()
            is AppState.LocalError -> showError(state)
            is AppState.ServerError -> showError(state)
        }
    }

    private fun showError(state: AppState) {
        ui.loading.gone()
        Toast.makeText(context, state.javaClass.simpleName, Toast.LENGTH_LONG).show()
    }

    private fun setRefreshBehaviour() {
        ui.swipeRefresh.setOnRefreshListener {
            viewModel.getFilmData()
            ui.swipeRefresh.isRefreshing = false
        }
    }

    private fun onFilmClickListener() = object : OnItemClickListener {
        override fun onClick(film: Film) {
            parentFragmentManager.beginTransaction().addToBackStack(this@MainFragment.tag)
                .replace(R.id.container, CurrentFilmFragment.newInstance(Bundle().apply {
                    putParcelable(KEY_FILM, film)
                })).commit()
        }
    }

    override fun onDestroy() {
        _ui = null
        super.onDestroy()
    }
}