package com.geekbrains.filmroulette.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.filmroulette.R
import com.geekbrains.filmroulette.model.Film

class FilmsAdapter() : RecyclerView.Adapter<BaseViewHolder>() {

    lateinit var itemClickListener: OnItemClickListener
    private var filmData: MutableList<Film> = mutableListOf(Film())
    fun setFilmData(data: MutableList<Film>) {
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

    override fun getItemCount(): Int {
        return filmData.size
    }
}

class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setData(film: Film) {
        itemView.findViewById<TextView>(R.id.film_title).text = film.name
        itemView.findViewById<TextView>(R.id.film_date).text = film.date.toString()
        itemView.findViewById<TextView>(R.id.film_rating).text = film.rate.toString()
        val like = itemView.findViewById<ImageView>(R.id.film_like)
        setLike(film, like)
        like.setOnClickListener {
            film.like = !film.like
            setLike(film, like)
        }

    }

    private fun setLike(film: Film, like: ImageView) {
        if (film.like) {
            like.setImageResource(R.drawable.ic_filled_like)
        } else {
            like.setImageResource(R.drawable.ic_like)
        }
    }
}