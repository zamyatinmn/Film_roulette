package com.geekbrains.filmroulette.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geekbrains.filmroulette.R
import com.geekbrains.filmroulette.databinding.FragmentMainBinding
import com.geekbrains.filmroulette.viewModel.AppState
import com.geekbrains.filmroulette.viewModel.MainViewModel

class MainFragment : Fragment() {
    private lateinit var ui: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: FilmsAdapter

    companion object {
        fun newInstance(): Fragment = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ui = FragmentMainBinding.inflate(inflater)
        initRecyclerView(ui.recyclerView)
        initRecyclerView(ui.recyclerView2)
        initRecyclerView(ui.recyclerView3)
        initRecyclerView(ui.recyclerView4)
        setRefreshBehaviour()
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getFilmData()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun renderData(state: AppState) {
        when (state) {
            is AppState.Success -> {
                ui.loading.visibility = View.GONE
                adapter.setData(state.data)
            }
            is AppState.Loading -> ui.loading.visibility = View.VISIBLE
            is AppState.LocalError -> {
                ui.loading.visibility = View.GONE
                Toast.makeText(context, state.javaClass.simpleName, Toast.LENGTH_LONG).show()
            }
            is AppState.ServerError -> {
                ui.loading.visibility = View.GONE
                Toast.makeText(context, state.javaClass.simpleName, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setRefreshBehaviour() {
        ui.swipeRefresh.setOnRefreshListener {
            viewModel.getFilmData()
            ui.swipeRefresh.isRefreshing = false
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        setHasOptionsMenu(true)
        val layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )

        recyclerView.layoutManager = layoutManager
        adapter = FilmsAdapter()
        adapter.itemClickListener = object : OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                parentFragmentManager.beginTransaction().addToBackStack("main")
                    .replace(R.id.container, CurrentFilmFragment.newInstance()).commit()
            }
        }
        recyclerView.adapter = adapter
    }
}