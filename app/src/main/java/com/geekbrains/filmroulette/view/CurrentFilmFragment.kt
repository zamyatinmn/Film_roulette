package com.geekbrains.filmroulette.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geekbrains.filmroulette.R
import com.geekbrains.filmroulette.databinding.FragmentCurrentFilmBinding
import com.geekbrains.filmroulette.model.Film

class CurrentFilmFragment : Fragment() {
    private lateinit var ui: FragmentCurrentFilmBinding

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
        val film: Film? = arguments?.getParcelable(KEY_FILM)
        film?.let {
            setData(film)
            ui.filmLike.setOnClickListener {
                film.like = !film.like
                if (film.like) ui.filmLike.setImageResource(R.drawable.ic_filled_like)
                else ui.filmLike.setImageResource(R.drawable.ic_like)
            }
        }
        return ui.root
    }

    private fun setData(film: Film) {
        ui.apply {
            name.text = film.name
            date.text = film.date.toString()
            filmRating.text = film.rate.toString()

            if (film.like) filmLike.setImageResource(R.drawable.ic_filled_like)
            else filmLike.setImageResource(R.drawable.ic_like)
        }
    }
}