package com.geekbrains.filmroulette.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.geekbrains.filmroulette.PLACE_OF_BIRTH_KEY
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

    private val bundle = Bundle()

    fun getLocationOnMap(location: String) {
        bundle.putString(PLACE_OF_BIRTH_KEY, location)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission()
        } else {
            showMap()
        }
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
                R.id.contacts -> {
                    changeScreen(ContactsFragment.newInstance())
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

    private fun showMap() {
        supportFragmentManager.beginTransaction()
            .addToBackStack("map")
            .add(ui.container.id, MapsFragment.newInstance(bundle))
            .commit()
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

//region===============Permissions=================================================================

    @RequiresApi(Build.VERSION_CODES.M)
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permitted ->
        val gps = permitted.getValue(Manifest.permission.ACCESS_FINE_LOCATION)
        when {
            gps -> {
                showMap()
            }
            !shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showGoSettings()
            }
            else -> {
                showRatio()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private val settingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { checkPermission() }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermission() {
        val resultCall =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (resultCall == PermissionChecker.PERMISSION_GRANTED) {
            showMap()
        } else {
            requestPermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showGoSettings() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.need_permission_header))
            .setMessage(
                "${getString(R.string.need_permission_text)} \n" +
                        getString(R.string.need_permission_text_again)
            )
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                openApplicationSettings()
            }
            .setNegativeButton(getString(android.R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        settingsLauncher.launch(appSettingsIntent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showRatio() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.need_permission_header))
            .setMessage(getString(R.string.need_permission_text))
            .setPositiveButton(getString(R.string.need_permission_give)) { _, _ ->
                requestPermission()
            }
            .setNegativeButton(getString(R.string.need_permission_dont_give)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
//endregion========================================================================================
}