package com.example.fitnesscalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_loading);
        Animation animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ImageView splashScreenLogo = findViewById(R.id.splash_screen_logo);

        splashScreenLogo.startAnimation(animFadeIn);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        setAppLanguageFromPreference();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setAppLanguageFromPreference() {
        String currentLanguage = getResources().getConfiguration().locale.getLanguage();
        String selectedLanguage = sharedPrefs.getString("language", "en");

        if (!selectedLanguage.equals(currentLanguage)) {
            updateAppLanguage(selectedLanguage);
        }
    }

    private void updateAppLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        android.content.res.Resources resources = getResources();
        android.content.res.Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);

        android.content.SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("language", language);
        editor.apply();
    }
}
