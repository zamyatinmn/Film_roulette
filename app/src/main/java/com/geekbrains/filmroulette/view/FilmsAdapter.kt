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
    lateinit var holder: BaseViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.film_card, parent, false)
        return BaseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(holder.itemView, position)
        }
        this.holder = holder
    }

    fun setData(film: Film) {
        holder.setData(film)
    }

    override fun getItemCount(): Int {
        return 20
    }
}

class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setData(film: Film) {
        itemView.findViewById<TextView>(R.id.film_title).text = film.name
        itemView.findViewById<TextView>(R.id.film_date).text = film.date.toString()
        itemView.findViewById<TextView>(R.id.film_rating).text = film.rate.toString()
        itemView.findViewById<ImageView>(R.id.film_date).setImageResource(R.drawable.ic_launcher_background)
//        itemView.findViewById<ImageView>(R.id.film_date).setImageDrawable(film.poster)
        if (film.like){
            itemView.findViewById<ImageView>(R.id.film_like).setImageResource(R.drawable.ic_like)
        } else {
            itemView.findViewById<ImageView>(R.id.film_like).setImageResource(R.drawable.ic_filled_like)
        }
    }
}