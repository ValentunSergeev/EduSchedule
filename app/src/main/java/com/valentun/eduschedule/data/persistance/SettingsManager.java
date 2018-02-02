package com.valentun.eduschedule.data.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import com.valentun.eduschedule.Constants;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.valentun.eduschedule.Constants.SETTINGS.SETTINGS_AUTO_CACHE;
import static com.valentun.eduschedule.Constants.SETTINGS.SETTINGS_NOTIFICATIONS;
import static com.valentun.eduschedule.Constants.SETTINGS.SETTINGS_SCHEDULE_TYPE;

public class SettingsManager {
    private SharedPreferences preferences;

    public SettingsManager(Context context) {
        preferences = getDefaultSharedPreferences(context);
    }

    public boolean isAutoUseCacheEnabled() {
        return preferences.getBoolean(SETTINGS_AUTO_CACHE, true);
    }

    public boolean isNotificationsEnabled() {
        return preferences.getBoolean(SETTINGS_NOTIFICATIONS, true);
    }

    public boolean isTypeChosen() {
        return  preferences.contains(SETTINGS_SCHEDULE_TYPE);
    }

    public void setPreferredScheduleType(String type) {
        preferences.edit().
                putString(SETTINGS_SCHEDULE_TYPE, type)
                .apply();
    }

    public String getPreferredScheduleType() {
        return preferences.getString(SETTINGS_SCHEDULE_TYPE, Constants.TYPE_NONE);
    }
}
