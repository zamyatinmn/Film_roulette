package com.geekbrains.filmroulette.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.filmroulette.POSTER_PREFIX
import com.geekbrains.filmroulette.R
import com.geekbrains.filmroulette.databinding.FragmentCurrentFilmBinding
import com.geekbrains.filmroulette.model.CurrentMovie
import com.geekbrains.filmroulette.viewModel.AppState
import com.geekbrains.filmroulette.viewModel.CurrentViewModel
import com.geekbrains.filmroulette.viewModel.MainViewModel
import com.squareup.picasso.Picasso

class CurrentFilmFragment : Fragment() {
    private lateinit var ui: FragmentCurrentFilmBinding
    private val viewModel: CurrentViewModel by lazy {
        ViewModelProvider(this).get(CurrentViewModel::class.java)
    }

    companion object {
        const val KEY_FILM = "KEY_FILM"
        fun newInstance(bundle: Bundle) = CurrentFilmFragment().apply { arguments = bundle }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ui = FragmentCurrentFilmBinding.inflate(inflater, container, false)
        return ui.root
    }

    private fun setData(film: CurrentMovie) {
        ui.apply {
            name.text = film.title
            date.text = film.release_date
            filmRating.text = film.vote_average.toString()
            description.text = film.overview
            originalName.text = film.original_title
            val tempBudget = "${getString(R.string.budget)} ${film.budget}$"
            budget.text = tempBudget
            val tempRevenue = "${getString(R.string.revenue)} ${film.revenue}$"
            revenue.text = tempRevenue
            val tempDuration = "${film.runtime} ${getString(R.string.minutes)}"
            duration.text = tempDuration
            val tempGenre = StringBuilder("")
            for (genre in film.genres) {
                tempGenre.append("${genre.name} ")
            }
            genres.text = tempGenre
            Picasso.get()
                .load("$POSTER_PREFIX${film.poster_path}")
                .into(poster)

            if (film.like) filmLike.setImageResource(R.drawable.ic_filled_like)
            else filmLike.setImageResource(R.drawable.ic_like)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filmID = arguments?.getLong(KEY_FILM)
        filmID?.apply {
            viewModel.getFilm(this, getString(R.string.language))
        }
        viewModel.getLiveData().observe(viewLifecycleOwner, {renderData(it)})
    }

    private fun renderData(appState: AppState){
        when(appState){
            is AppState.Loading -> {
                ui.loading.visible()
            }
            is AppState.SuccessCurrent ->{
                ui.loading.gone()
                setData(appState.film)
//                ui.filmLike.setOnClickListener {
//                    appState.film.like = !appState.film.like
//                    if (appState.film.like) ui.filmLike.setImageResource(R.drawable.ic_filled_like)
//                    else ui.filmLike.setImageResource(R.drawable.ic_like)
//                }
            }
        }
    }
}