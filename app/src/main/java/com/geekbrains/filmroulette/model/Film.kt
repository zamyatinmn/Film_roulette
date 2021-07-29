package com.geekbrains.filmroulette.model

import android.graphics.drawable.Drawable

class Film(
//    val poster: Drawable = getDefaultDrawable(),
    val name: String = "Film name",
    val date: Int = 1970,
    val rate: Float = 5.0f,
    var like: Boolean = false
){
    //temporary plug
    private fun getDefaultDrawable(): Drawable = Drawable.createFromPath("/a/b/c.png")!!
}



