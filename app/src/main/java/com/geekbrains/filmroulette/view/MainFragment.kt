package com.geekbrains.filmroulette.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.filmroulette.*
import com.geekbrains.filmroulette.databinding.FragmentMainBinding
import com.geekbrains.filmroulette.model.FILMS_EXTRA
import com.geekbrains.filmroulette.model.MainService
import com.geekbrains.filmroulette.model.MovieResult
import com.geekbrains.filmroulette.model.Results
import com.geekbrains.filmroulette.view.CurrentFilmFragment.Companion.KEY_FILM
import com.geekbrains.filmroulette.viewModel.MainViewModel

const val INTENT_FILTER = "INTENT FILTER"
const val LOAD_RESULT_EXTRA = "LOAD RESULT"
const val INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val URL_MALFORMED_EXTRA = "URL MALFORMED"
const val RESPONSE_SUCCESS_NOVELTY_EXTRA = "RESPONSE_SUCCESS_NOVELTY_EXTRA"
const val RESPONSE_SUCCESS_POPULAR_EXTRA = "RESPONSE_SUCCESS_POPULAR_EXTRA"
const val RESPONSE_SUCCESS_THRILLER_EXTRA = "RESPONSE_SUCCESS_THRILLER_EXTRA"
const val RESPONSE_SUCCESS_COMEDY_EXTRA = "RESPONSE_SUCCESS_COMEDY_EXTRA"

const val RESPONSE_EXTRA = "RESPONSE_EXTRA"
private const val PROCESS_ERROR = "Обработка ошибки"

class MainFragment : Fragment() {

    private var _ui: FragmentMainBinding? = null
    private val ui get() = _ui!!
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var noveltyAdapter: FilmsAdapter = FilmsAdapter()
    private var popularAdapter: FilmsAdapter = FilmsAdapter()
    private var thrillerAdapter: FilmsAdapter = FilmsAdapter()
    private var comedyAdapter: FilmsAdapter = FilmsAdapter()

    companion object {
        fun newInstance(): Fragment = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _ui = FragmentMainBinding.inflate(inflater)

        ui.apply {
            initRecyclerView(noveltyRecyclerView, noveltyAdapter)
            initRecyclerView(popularRecyclerView, popularAdapter)
            initRecyclerView(thrillerRecyclerView, thrillerAdapter)
            initRecyclerView(comedyRecyclerView, comedyAdapter)
        }

        setRefreshBehaviour()
        return ui.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultsReceiver, IntentFilter(INTENT_FILTER))
        }
    }

    private fun getData() {
        ui.loading.visible()
        getDataFromService("$DISCOVER_PREFIX$API_KEY_NAME${BuildConfig.API_KEY}&sort_by=release_date.desc&language=ru-RU&year=2021")
        getDataFromService("$DISCOVER_PREFIX$API_KEY_NAME${BuildConfig.API_KEY}&language=ru-RU&sort_by=popularity.desc")
        getDataFromService("$DISCOVER_PREFIX$API_KEY_NAME${BuildConfig.API_KEY}&language=ru-RU&with_genres=53")
        getDataFromService("$DISCOVER_PREFIX$API_KEY_NAME${BuildConfig.API_KEY}&language=ru-RU&with_genres=35")
    }

    private fun getDataFromService(request: String) {
        context?.let {
            it.startService(Intent(it, MainService::class.java).apply {
                putExtra(FILMS_EXTRA, request)
            })
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView, adapter: FilmsAdapter) {
        recyclerView.setHasFixedSize(true)
        setHasOptionsMenu(true)
        val layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )
        recyclerView.layoutManager = layoutManager
        adapter.itemClickListener = onFilmClickListener()
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setRefreshBehaviour() {
        ui.swipeRefresh.setOnRefreshListener {
            getData()
            ui.loading.visible()
            ui.swipeRefresh.isRefreshing = false
        }
    }

    private fun onFilmClickListener() = object : OnItemClickListener {
        override fun onClick(film: MovieResult) {
            parentFragmentManager.beginTransaction().addToBackStack(this@MainFragment.tag)
                .replace(R.id.container, CurrentFilmFragment.newInstance(Bundle().apply {
                    putParcelable(KEY_FILM, viewModel.getFilm(film.id))
                })).commit()
        }
    }

    override fun onDestroy() {
        _ui = null
        super.onDestroy()

        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
    }

    private val loadResultsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(LOAD_RESULT_EXTRA)) {
                INTENT_EMPTY_EXTRA -> Log.e(INTENT_EMPTY_EXTRA, PROCESS_ERROR)
                DATA_EMPTY_EXTRA -> Log.e(DATA_EMPTY_EXTRA, PROCESS_ERROR)
                RESPONSE_EMPTY_EXTRA -> Log.e(RESPONSE_EMPTY_EXTRA, PROCESS_ERROR)
                REQUEST_ERROR_EXTRA -> Log.e(REQUEST_ERROR_EXTRA, PROCESS_ERROR)
                REQUEST_ERROR_MESSAGE_EXTRA -> Log.e(REQUEST_ERROR_MESSAGE_EXTRA, PROCESS_ERROR)
                URL_MALFORMED_EXTRA -> Log.e(URL_MALFORMED_EXTRA, PROCESS_ERROR)
                RESPONSE_SUCCESS_NOVELTY_EXTRA -> {
                    val result = intent.getParcelableExtra<Results>(RESPONSE_EXTRA)
                    renderData(result!!.results, noveltyAdapter)
                }
                RESPONSE_SUCCESS_POPULAR_EXTRA -> {
                    val result = intent.getParcelableExtra<Results>(RESPONSE_EXTRA)
                    renderData(result!!.results, popularAdapter)
                }
                RESPONSE_SUCCESS_THRILLER_EXTRA -> {
                    val result = intent.getParcelableExtra<Results>(RESPONSE_EXTRA)
                    renderData(result!!.results, thrillerAdapter)
                }
                RESPONSE_SUCCESS_COMEDY_EXTRA -> {
                    val result = intent.getParcelableExtra<Results>(RESPONSE_EXTRA)
                    renderData(result!!.results, comedyAdapter)
                }
                else -> Log.e(PROCESS_ERROR, PROCESS_ERROR)
            }
        }
    }

    private fun renderData(list: MutableList<MovieResult>, adapter: FilmsAdapter) {
        ui.loading.gone()
        adapter.setFilmData(list)
    }
}