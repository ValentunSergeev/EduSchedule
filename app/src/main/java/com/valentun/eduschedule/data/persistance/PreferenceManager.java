package com.valentun.eduschedule.data.persistance;


import android.content.Context;
import android.content.SharedPreferences;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PreferenceManager {
    private static final String GROUP_KEY = "SELECTED_GROUP";
    private static final String SCHOOL_KEY = "SELECTED_SCHOOL";
    private static final String SCHEDULE_KEY = "CACHED_SCHEDULE";
    private static final String SCHEDULE_TIME_KEY = "CACHED_SCHEDULE_DATE";

    private static final int DEFAULT_INT = -1;

    private SharedPreferences preferences;
    private Context context;

    public PreferenceManager(Context context) {
        this.context = context;
        preferences = getDefaultSharedPreferences(context);
    }

    public boolean isGroupChosen() {
        return preferences.contains(GROUP_KEY);
    }

    public void setGroup(String groupID) {
        preferences.edit().putString(GROUP_KEY, groupID).apply();
    }

    public String getGroupId() {
        return preferences.getString(GROUP_KEY, null);
    }

    public void clearGroup() {
        preferences.edit().remove(GROUP_KEY).apply();
    }

    public void clearSchool() {
        preferences.edit()
                .remove(SCHOOL_KEY)
                .remove(GROUP_KEY)
                .remove(SCHEDULE_KEY)
                .remove(SCHEDULE_TIME_KEY)
                .apply();
    }

    public void clearCachedSchedule() {
        preferences.edit()
                .remove(SCHEDULE_KEY)
                .apply();
    }

    public void setSchool(int groupId) {
        preferences.edit().putInt(SCHOOL_KEY, groupId).apply();
    }

    public int getSchoolId() {
        return preferences.getInt(SCHOOL_KEY, DEFAULT_INT);
    }


    public boolean isSchoolChosen() {
        return preferences.contains(SCHOOL_KEY);
    }

    public void cacheSchedule(String schedule) {
        preferences.edit()
                .putString(SCHEDULE_KEY, schedule)
                .putLong(SCHEDULE_TIME_KEY, System.currentTimeMillis())
                .apply();
    }

    public boolean isHasCachedSchedule() {
        return preferences.contains(SCHEDULE_KEY);
    }

    public String getCachedSchedule() {
        return preferences.getString(SCHEDULE_KEY, null);
    }

    public long getCachedTime() {
        return preferences.getLong(SCHEDULE_TIME_KEY, -1);
    }
}
