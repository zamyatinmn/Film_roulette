package com.geekbrains.filmroulette.view

import android.view.View

interface OnItemClickListener {
    fun onClick(view: View, position: Int)
}