package com.geekbrains.filmroulette.model

import android.os.Handler
import android.os.Looper
import com.geekbrains.filmroulette.API_KEY_NAME
import com.geekbrains.filmroulette.DISCOVER_PREFIX
import com.geekbrains.filmroulette.BuildConfig
import com.geekbrains.filmroulette.MOVIE_PREFIX
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection


/**
 * Created by Maxim Zamyatin on 07.08.2021
 */

class ApiRepository : IRepository {
    private lateinit var noveltyResult: Results
    private lateinit var popularResult: Results
    private lateinit var thrillerResult: Results
    private lateinit var comedyResult: Results
    private lateinit var movieResult: CurrentMovie

    override fun getFilmFromServer(movieID: Long): CurrentMovie {
        getData("$MOVIE_PREFIX$movieID?$API_KEY_NAME${BuildConfig.API_KEY}&language=ru-RU")
        return movieResult
    }

    private fun getData(string: String) {
        val handler = Handler(Looper.getMainLooper())

        val thread = Thread {
            try {
                val url = URL(string)
                val connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
                connection.connectTimeout = 5000
                connection.requestMethod = "GET"
                val buffer = BufferedReader(InputStreamReader(connection.inputStream))

                when (string[string.length - 1]) {
                    '1' -> noveltyResult = Gson().fromJson(buffer, Results::class.java)
                    'c' -> popularResult = Gson().fromJson(buffer, Results::class.java)
                    '3' -> thrillerResult = Gson().fromJson(buffer, Results::class.java)
                    '5' -> comedyResult = Gson().fromJson(buffer, Results::class.java)
                    'U' -> movieResult = Gson().fromJson(buffer, CurrentMovie::class.java)
                }

                handler.post {
                    //onLoad
                }
            } catch (e: Exception) {
                handler.post {
                    //onFail
                }
            }
        }
        thread.start()
        thread.join()
    }

    override fun getNovelty(): MutableList<MovieResult> {
        getData("$DISCOVER_PREFIX$API_KEY_NAME${BuildConfig.API_KEY}&sort_by=release_date.desc&language=ru-RU&year=2021")
        return noveltyResult.results
    }

    override fun getPopular(): MutableList<MovieResult> {
        getData("$DISCOVER_PREFIX$API_KEY_NAME${BuildConfig.API_KEY}&language=ru-RU&sort_by=popularity.desc")
        return popularResult.results
    }

    override fun getThriller(): MutableList<MovieResult> {
        getData("$DISCOVER_PREFIX$API_KEY_NAME${BuildConfig.API_KEY}&language=ru-RU&with_genres=53")
        return thrillerResult.results
    }

    override fun getComedy(): MutableList<MovieResult> {
        getData("$DISCOVER_PREFIX$API_KEY_NAME${BuildConfig.API_KEY}&language=ru-RU&with_genres=35")
        return comedyResult.results
    }
}