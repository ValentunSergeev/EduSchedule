package com.valentun.eduschedule.data.persistance;


import android.content.Context;
import android.content.SharedPreferences;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PreferenceManager {
    private static final String MY_SCHEDULE_OBJECT_KEY = "SELECTED_GROUP";
    private static final String SCHOOL_KEY = "SELECTED_SCHOOL";
    private static final String SCHEDULE_KEY = "CACHED_SCHEDULE";
    private static final String SCHEDULE_TIME_KEY = "CACHED_SCHEDULE_DATE";
    private static final String SCHEDULE_PATH = "SCHEDULE_PATH";

    private static final int DEFAULT_INT = -1;

    private SharedPreferences preferences;

    public PreferenceManager(Context context) {
        preferences = getDefaultSharedPreferences(context);
    }

    public boolean isObjectChosen() {
        return preferences.contains(MY_SCHEDULE_OBJECT_KEY);
    }

    public void setMyScheduleObject(String objectId) {
        preferences.edit().putString(MY_SCHEDULE_OBJECT_KEY, objectId).apply();
    }

    public String getObjectId() {
        return preferences.getString(MY_SCHEDULE_OBJECT_KEY, null);
    }

    public void clearMyScheduleObject() {
        preferences.edit().remove(MY_SCHEDULE_OBJECT_KEY).apply();
    }

    public void clearSchool() {
        preferences.edit()
                .remove(SCHOOL_KEY)
                .remove(MY_SCHEDULE_OBJECT_KEY)
                .remove(SCHEDULE_KEY)
                .remove(SCHEDULE_TIME_KEY)
                .remove(SCHEDULE_PATH)
                .apply();
    }

    public void clearCachedSchedule() {
        preferences.edit()
                .remove(SCHEDULE_KEY)
                .apply();
    }

    public void setSchool(int schoolId) {
        preferences.edit().putInt(SCHOOL_KEY, schoolId).apply();
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

    public void savePath(String path) {
        preferences.edit()
                .putString(SCHEDULE_PATH, path)
                .apply();
    }

    public String getSavedPath() {
        return preferences.getString(SCHEDULE_PATH, null);
    }
}
