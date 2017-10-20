package com.valentun.eduschedule.data.persistance;


import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class PreferenceManager {
    private Context context;

    public PreferenceManager(Context context) {
        this.context = context;
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
}
