package com.geekbrains.filmroulette.view

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geekbrains.filmroulette.ADULT_MODE_KEY
import com.geekbrains.filmroulette.App
import com.geekbrains.filmroulette.databinding.FragmentSettingsBinding


/**
 * Created by Maxim Zamyatin on 18.08.2021
 */


class SettingsFragment : Fragment() {
    private var _ui: FragmentSettingsBinding? = null
    private val ui get() = _ui!!

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _ui = FragmentSettingsBinding.inflate(inflater, container, false)
        return ui.root
    }

    private lateinit var sharedPreferences : SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity()
            .getSharedPreferences(SettingsFragment::class.java.simpleName, MODE_PRIVATE)

        App.isAdultMode = sharedPreferences.getBoolean(ADULT_MODE_KEY, false)

        ui.adultCb.isChecked = App.isAdultMode
        ui.adultCb.setOnCheckedChangeListener { _, isChecked -> App.isAdultMode = isChecked }
    }

    override fun onStop() {
        super.onStop()
        sharedPreferences.edit().putBoolean(ADULT_MODE_KEY, App.isAdultMode).apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        _ui = null
    }
}