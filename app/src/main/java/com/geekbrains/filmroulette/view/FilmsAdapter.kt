package com.geekbrains.filmroulette.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.filmroulette.POSTER_PREFIX
import com.geekbrains.filmroulette.R
import com.geekbrains.filmroulette.model.MovieResult
import com.squareup.picasso.Picasso

class FilmsAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    lateinit var itemClickListener: OnItemClickListener
    private var filmData = mutableListOf(
        MovieResult(
            true,
            "",
            listOf(21),
            1,
            "",
            "",
            "",
            1.0,
            "",
            "",
            "",
            false,
            1.0,
            1L
        )
    )

    fun setFilmData(data: MutableList<MovieResult>) {
        filmData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.film_card, parent, false)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(filmData[position])
        }
        holder.setData(filmData[position])
    }

    override fun getItemCount() = filmData.size
}

class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setData(film: MovieResult) {
        itemView.apply {
            findViewById<TextView>(R.id.film_title).text = film.title
            findViewById<TextView>(R.id.film_date).text = film.release_date
            findViewById<TextView>(R.id.film_rating).text = film.vote_average.toString()
            Picasso.get()
                .load("$POSTER_PREFIX${film.poster_path}")
                .into(findViewById<ImageView>(R.id.poster))
        }
        val like = itemView.findViewById<ImageView>(R.id.film_like)
        setLike(film, like)
        like.setOnClickListener {
            film.like = !film.like
            setLike(film, like)
        }

    }

    private fun setLike(film: MovieResult, like: ImageView) {
        if (film.like) like.setImageResource(R.drawable.ic_filled_like)
        else like.setImageResource(R.drawable.ic_like)
    }
}