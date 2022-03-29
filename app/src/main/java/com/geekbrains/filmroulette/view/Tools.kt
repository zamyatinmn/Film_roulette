package com.geekbrains.filmroulette.view

import android.view.View
//Немного побаловался с вновь узнанными фичами, удобные штуки
fun View.invisible() {
    this.visibility = View.INVISIBLE
}
fun View.visible() {
    this.visibility = View.VISIBLE
}
fun View.gone() {
    this.visibility = View.GONE
}

fun View.setVisible(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

operator fun View.unaryPlus(){
    if (this.visibility == View.GONE) this.visibility = View.INVISIBLE
    if (this.visibility == View.INVISIBLE) this.visibility = View.VISIBLE
}

operator fun View.unaryMinus(){
    if (this.visibility == View.VISIBLE) this.visibility = View.INVISIBLE
    if (this.visibility == View.INVISIBLE) this.visibility = View.GONE
}

//тест