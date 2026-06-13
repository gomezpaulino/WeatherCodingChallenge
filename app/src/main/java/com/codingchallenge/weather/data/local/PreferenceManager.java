package com.codingchallenge.weather.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferenceManager {
    private static final String PREF_NAME = "weather_prefs";
    private static final String KEY_LAST_CITY = "last_city";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_UNITS = "units"; // "metric" or "imperial"
    private final SharedPreferences sharedPreferences;

    @Inject
    public PreferenceManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLastCity(String cityName) {
        sharedPreferences.edit().putString(KEY_LAST_CITY, cityName).apply();
    }

    public String getLastCity() {
        return sharedPreferences.getString(KEY_LAST_CITY, null);
    }

    public void saveDarkMode(boolean isDarkMode) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, isDarkMode).apply();
    }

    public boolean isDarkMode() {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false);
    }

    public void saveUnits(String units) {
        sharedPreferences.edit().putString(KEY_UNITS, units).apply();
    }

    public String getUnits() {
        return sharedPreferences.getString(KEY_UNITS, "imperial");
    }
}
