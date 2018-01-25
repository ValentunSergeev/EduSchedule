package com.valentun.eduschedule.data.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.valentun.eduschedule.Constants.SETTINGS.SETTINGS_AUTO_CACHE;

public class SettingsManager {
    private SharedPreferences preferences;

    public SettingsManager(Context context) {
        preferences = getDefaultSharedPreferences(context);
    }

    public boolean isAutoUseCacheEnabled() {
        return preferences.getBoolean(SETTINGS_AUTO_CACHE, false);
    }
}
