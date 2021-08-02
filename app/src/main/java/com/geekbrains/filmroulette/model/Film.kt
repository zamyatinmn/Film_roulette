package com.geekbrains.filmroulette.model

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
//    val poster: Drawable = getDefaultDrawable(),
    val name: String = "Film name",
    val date: Int = 1970,
    val rate: Float = 5.0f,
    var like: Boolean = false
) : Parcelable {
    //temporary plug
    private fun getDefaultDrawable(): Drawable = Drawable.createFromPath("/a/b/c.png")!!
}



