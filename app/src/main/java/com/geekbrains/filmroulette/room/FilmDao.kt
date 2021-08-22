package com.geekbrains.filmroulette.room

import androidx.room.*


/**
 * Created by Maxim Zamyatin on 18.08.2021
 */

@Dao
interface FilmDao {
    @Query("SELECT * FROM FilmEntity")
    fun all(): List<FilmEntity>

    @Query("SELECT * FROM FilmEntity WHERE id LIKE :id")
    fun getDataByID(id: Long): List<FilmEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(filmEntity: FilmEntity)

    @Update
    fun update(filmEntity: FilmEntity)

    @Delete
    fun delete(filmEntity: FilmEntity)

}