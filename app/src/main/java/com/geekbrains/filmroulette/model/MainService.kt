package com.geekbrains.filmroulette.model

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.geekbrains.filmroulette.BuildConfig
import com.geekbrains.filmroulette.view.*
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


/**
 * Created by Maxim Zamyatin on 16.08.2021
 */

const val FILMS_EXTRA = "films_extra"
private const val REQUEST_GET = "GET"
private const val REQUEST_TIMEOUT = 10000
private const val REQUEST_API_KEY = "X-Yandex-API-Key"

class MainService(name: String = "MainService") : IntentService(name) {

    private val broadcastIntent = Intent(INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val request = intent.getStringExtra(FILMS_EXTRA)
            if (request == null || request == "") {
                onEmptyData()
            } else {
                loadData(request)
                loadData(request)
                loadData(request)
                loadData(request)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadData(request: String) {
        try {
            val uri = URL(request)
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.apply {
                    requestMethod = REQUEST_GET
                    readTimeout = REQUEST_TIMEOUT
                    addRequestProperty(REQUEST_API_KEY, BuildConfig.API_KEY)
                }

                val results: Results =
                    Gson().fromJson(
                        getLines(BufferedReader(InputStreamReader(urlConnection.inputStream))),
                        Results::class.java
                    )
                when (request[request.length - 1]) {
                    '1' -> onResponseNovelty(results)
                    'c' -> onResponsePopular(results)
                    '3' -> onResponseThriller(results)
                    '5' -> onResponseComedy(results)
                }
            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            onMalformedURL()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    private fun onResponseNovelty(results: Results) {
        putLoadResult(RESPONSE_SUCCESS_NOVELTY_EXTRA)
        broadcastIntent.putExtra(RESPONSE_EXTRA, results)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onResponsePopular(results: Results) {
        putLoadResult(RESPONSE_SUCCESS_POPULAR_EXTRA)
        broadcastIntent.putExtra(RESPONSE_EXTRA, results)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onResponseThriller(results: Results) {
        putLoadResult(RESPONSE_SUCCESS_THRILLER_EXTRA)
        broadcastIntent.putExtra(RESPONSE_EXTRA, results)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onResponseComedy(results: Results) {
        putLoadResult(RESPONSE_SUCCESS_COMEDY_EXTRA)
        broadcastIntent.putExtra(RESPONSE_EXTRA, results)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onMalformedURL() {
        putLoadResult(URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(REQUEST_ERROR_MESSAGE_EXTRA, error)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyIntent() {
        putLoadResult(INTENT_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyData() {
        putLoadResult(DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(LOAD_RESULT_EXTRA, result)
    }
}