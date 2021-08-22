package com.geekbrains.filmroulette.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.geekbrains.filmroulette.ListConverter


/**
 * Created by Maxim Zamyatin on 18.08.2021
 */

@Database(entities = [FilmEntity::class], version = 1, exportSchema = false)
@TypeConverters(ListConverter::class)

abstract class FilmDatabase : RoomDatabase() {

    abstract fun filmDao(): FilmDao
}