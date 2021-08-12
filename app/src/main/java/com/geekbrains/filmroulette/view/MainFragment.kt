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
    private lateinit var ui: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var noveltyAdapter: FilmsAdapter
    private lateinit var popularAdapter: FilmsAdapter
    private lateinit var thrillerAdapter: FilmsAdapter
    private lateinit var comedyAdapter: FilmsAdapter

    companion object {
        fun newInstance(): Fragment = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ui = FragmentMainBinding.inflate(inflater)
        initNoveltyRecyclerView(ui.noveltyRecyclerView)
        initPopularRecyclerView(ui.popularRecyclerView)
        initThrillerRecyclerView(ui.thrillerRecyclerView)
        initComedyRecyclerView(ui.comedyRecyclerView)
        setRefreshBehaviour()
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getFilmData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Success -> {
                ui.loading.visibility = View.GONE
                noveltyAdapter.setFilmData(state.novelty)
                popularAdapter.setFilmData(state.popular)
                thrillerAdapter.setFilmData(state.thriller)
                comedyAdapter.setFilmData(state.comedy)
            }
            is AppState.Loading -> ui.loading.visibility = View.VISIBLE
            is AppState.LocalError -> {
                ui.loading.visibility = View.GONE
                Toast.makeText(context, state.javaClass.simpleName, Toast.LENGTH_LONG).show()
            }
            is AppState.ServerError -> {
                ui.loading.visibility = View.GONE
                Toast.makeText(context, state.javaClass.simpleName, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setRefreshBehaviour() {
        ui.swipeRefresh.setOnRefreshListener {
            viewModel.getFilmData()
            ui.swipeRefresh.isRefreshing = false
        }
    }

    private fun initNoveltyRecyclerView(recyclerView: RecyclerView) {
        prepareRecycler(recyclerView)
        noveltyAdapter = FilmsAdapter()
        noveltyAdapter.itemClickListener = onFilmClickListener()
        recyclerView.adapter = noveltyAdapter
    }

    private fun initPopularRecyclerView(recyclerView: RecyclerView) {
        prepareRecycler(recyclerView)
        popularAdapter = FilmsAdapter()
        popularAdapter.itemClickListener = onFilmClickListener()
        recyclerView.adapter = popularAdapter
    }

    private fun initThrillerRecyclerView(recyclerView: RecyclerView) {
        prepareRecycler(recyclerView)
        thrillerAdapter = FilmsAdapter()
        thrillerAdapter.itemClickListener = onFilmClickListener()
        recyclerView.adapter = thrillerAdapter
    }

    private fun initComedyRecyclerView(recyclerView: RecyclerView) {
        prepareRecycler(recyclerView)
        comedyAdapter = FilmsAdapter()
        comedyAdapter.itemClickListener = onFilmClickListener()
        recyclerView.adapter = comedyAdapter
    }

    private fun onFilmClickListener() = object : OnItemClickListener {
        override fun onClick(film: Film) {
            val bundle = Bundle()
            bundle.putParcelable(KEY_FILM, film)
            parentFragmentManager.beginTransaction().addToBackStack("main")
                .replace(R.id.container, CurrentFilmFragment.newInstance(bundle)).commit()
        }
    }

    private fun prepareRecycler(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        setHasOptionsMenu(true)
        val layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        recyclerView.layoutManager = layoutManager
    }
}