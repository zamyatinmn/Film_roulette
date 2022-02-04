package com.geekbrains.filmroulette

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.StringBuilder
import java.util.*
import java.util.stream.Collectors


/**
 * Created by Maxim Zamyatin on 18.08.2021
 */


class ListConverter {
    companion object {

        @JvmStatic
        @TypeConverter
        fun toList(data: String): List<Long> {
            val genres: List<String> = data.split(", ")
            val result: MutableList<Long> = mutableListOf()
            for (genre in genres){
                result.add(genre.replace(" ", "").toLong())
            }
            return result
        }

        @JvmStatic
        @TypeConverter
        fun fromList(genres: List<Long>): String {
            val sb = StringBuilder("")
            for (genre in genres){
                sb.append(genre).append(", ")
            }
            sb.delete(sb.length-2, sb.length-1)
            return sb.toString()
        }


    }


}