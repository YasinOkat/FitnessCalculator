package com.example.fitnesscalculator.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.fitnesscalculator.R
import java.util.Locale

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)

        val languagePreference = findPreference<ListPreference>("language_preference")
        languagePreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()

        val sharedPreferences = preferenceScreen.sharedPreferences
        sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == "language_preference") {
            val selectedLanguage = sharedPreferences.getString(key, "")
            setLocale(selectedLanguage)
            activity?.recreate()
        }
    }

    private fun setLocale(languageCode: String?) {
        languageCode?.let {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val resources = context?.resources
            val configuration = resources?.configuration
            configuration?.setLocale(locale)
            resources?.updateConfiguration(configuration, resources.displayMetrics)

            // Save the selected language in shared preferences
            val sharedPreferences = preferenceManager.sharedPreferences
            val editor = sharedPreferences?.edit()
            editor?.putString("language", languageCode)
            editor?.apply()
        }
    }




    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }
}

