package com.example.fitnesscalculator.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.fitnesscalculator.R
import androidx.preference.ListPreference
import androidx.preference.Preference
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.SharedPreferences

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
            val languagePreference = findPreference<ListPreference>(key)
            languagePreference?.summaryProvider = ListPreference.SimpleSummaryProvider.getInstance()
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

