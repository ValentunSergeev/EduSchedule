package com.valentun.eduschedule.data.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class SettingsManager {
    private SharedPreferences preferences;

    private static final String SETTINGS_AUTO_CACHE = "auto_use_cache";

    public SettingsManager(Context context) {
        preferences = getDefaultSharedPreferences(context);
    }

    public boolean isAutoUseCacheEnabled() {
        return preferences.getBoolean(SETTINGS_AUTO_CACHE, false);
    }
}
