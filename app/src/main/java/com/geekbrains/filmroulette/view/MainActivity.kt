package com.geekbrains.filmroulette.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.geekbrains.filmroulette.R
import com.geekbrains.filmroulette.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var ui: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)
        changeScreen(MainFragment.newInstance())
        setBottomNavigation()
        setSupportActionBar(ui.toolbar)
    }


    private fun setBottomNavigation() {
        ui.bottomNavigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.main -> {
                    changeScreen(MainFragment.newInstance())
                    true
                }
                R.id.favorites -> {
                    changeScreen(FavoritesFragment.newInstance())
                    true
                }
                R.id.want_see -> {
                    changeScreen(DeferredFragment.newInstance())
                    true
                }
                else -> false
            }
        }
    }

    private fun changeScreen(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().replace(ui.container.id, fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> true//do search
            R.id.settings -> {
                supportFragmentManager.beginTransaction()
                    .addToBackStack("settings")
                    .replace(ui.container.id, SettingsFragment.newInstance())
                    .commit()
                true
            }
            else -> false
        }
    }
}