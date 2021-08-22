package com.geekbrains.filmroulette

import android.app.Application
import androidx.room.Room
import com.geekbrains.filmroulette.room.FilmDao
import com.geekbrains.filmroulette.room.FilmDatabase


/**
 * Created by Maxim Zamyatin on 18.08.2021
 */

class App: Application() {
    companion object{
        var isAdultMode = false
        var app : App? = null
        var db : FilmDatabase? = null
        var dbName = "FilmRouletteDB"

        fun getDao(): FilmDao{
            if (db == null){
                synchronized(FilmDatabase::class.java){
                    db = Room.databaseBuilder(app!!.applicationContext, FilmDatabase::class.java, dbName)
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return db!!.filmDao()
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}