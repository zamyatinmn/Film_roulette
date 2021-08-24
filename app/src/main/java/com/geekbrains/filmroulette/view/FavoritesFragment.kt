package com.geekbrains.filmroulette.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.geekbrains.filmroulette.R
import com.geekbrains.filmroulette.databinding.FragmentFavoritesBinding
import com.geekbrains.filmroulette.model.MovieResult
import com.geekbrains.filmroulette.viewModel.AppState
import com.geekbrains.filmroulette.viewModel.FavoritesViewModel

class FavoritesFragment : Fragment() {
    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private var _ui: FragmentFavoritesBinding? = null
    private val ui get() = _ui!!

    private val viewModel: FavoritesViewModel by lazy {
        ViewModelProvider(this).get(FavoritesViewModel::class.java)
    }

    private val adapter = FilmsAdapter()

    private fun onFilmClickListener() = object : OnItemClickListener {
        override fun onClick(film: MovieResult) {
            parentFragmentManager.beginTransaction().addToBackStack(this@FavoritesFragment.tag)
                .replace(R.id.container, CurrentFilmFragment.newInstance(Bundle().apply {
                    putLong(CurrentFilmFragment.KEY_FILM, film.id)
                })).commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _ui = FragmentFavoritesBinding.inflate(inflater, container, false)
        viewModel.getData()
        setHasOptionsMenu(true)
        ui.recycler.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        ui.recycler.adapter = adapter

        ui.swipeRefresh.setOnRefreshListener {
            viewModel.getData()
            adapter.notifyDataSetChanged()
            ui.swipeRefresh.isRefreshing = false
        }
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })

        adapter.itemClickListener = onFilmClickListener()
        adapter.likeClickListener = object : OnClickLike {
            override fun onClick(film: MovieResult) {
                if (film.like) {
                    viewModel.saveFilmToDB(film)
                } else {
                    viewModel.deleteFilmFromDB(film)
                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                adapter.setFilmData(
                    appState.result,
                    object : OnClickLike {
                        override fun onClick(film: MovieResult) {
                            if (film.like) {
                                viewModel.saveFilmToDB(film)
                            } else {
                                viewModel.deleteFilmFromDB(film)
                            }
                        }
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _ui = null
    }
}