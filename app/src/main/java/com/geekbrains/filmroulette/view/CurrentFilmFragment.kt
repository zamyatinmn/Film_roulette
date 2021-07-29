package com.geekbrains.filmroulette.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geekbrains.filmroulette.R
import com.geekbrains.filmroulette.databinding.FragmentCurrentFilmBinding

class CurrentFilmFragment : Fragment() {
    private lateinit var ui: FragmentCurrentFilmBinding
    private var like = false

    companion object {
        fun newInstance(): Fragment = CurrentFilmFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ui = FragmentCurrentFilmBinding.inflate(inflater, container, false)
        ui.filmLike.setOnClickListener{
            if (like){
                ui.filmLike.setImageResource(R.drawable.ic_like)
            } else {
                ui.filmLike.setImageResource(R.drawable.ic_filled_like)
            }
            like = !like
        }
        return ui.root
    }
}