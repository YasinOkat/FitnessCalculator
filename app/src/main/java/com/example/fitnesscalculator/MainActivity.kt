package com.example.fitnesscalculator

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.fitnesscalculator.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_body_fat,
                R.id.navigation_calorie_calc,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        setAppLanguageFromPreference()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                navController.navigate(R.id.navigation_settings)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        setAppLanguageFromPreference()
    }

    private fun setAppLanguageFromPreference() {
        val currentLanguage = resources.configuration.locale.language
        val selectedLanguage = sharedPrefs.getString("language", "en")

        if (selectedLanguage != currentLanguage) {
            updateAppLanguage(selectedLanguage)
            recreate()
        }
    }



    private fun updateAppLanguage(language: String?) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = baseContext.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)

        val editor = sharedPrefs.edit()
        editor.putString("language", language)
        editor.apply()
    }



    private fun saveLanguageToPreference(language: String?) {
        sharedPrefs.edit().putString("language", language).apply()
    }
}
