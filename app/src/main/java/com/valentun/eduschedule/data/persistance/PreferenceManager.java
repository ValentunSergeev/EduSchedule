package com.valentun.eduschedule.data.persistance;


import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.io.InputStream;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class PreferenceManager {
    private static final String GROUP_KEY = "SELECTED_GROUP";

    private SharedPreferences preferences;
    private Context context;

    public PreferenceManager(Context context) {
        this.context = context;
        preferences = getDefaultSharedPreferences(context);
    }

    // TODO remove
    public String getTestData() {
        String json;
        try {
            InputStream is = context.getAssets().open("test_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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
}
