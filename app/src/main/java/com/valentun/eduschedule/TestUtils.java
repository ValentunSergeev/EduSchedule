package com.valentun.eduschedule;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("ALL")
public class TestUtils {
    public static String getTestData(Context context) {
        String json = null;
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
