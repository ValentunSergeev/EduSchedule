package com.valentun.eduschedule.utils;

import com.valentun.eduschedule.Constants;

public class NavigationUtils {
    public static String getScreenKeyFromScheduleType(String type) {
        String screen = null;

        switch (type) {
            case Constants.TYPE_TEACHER:
                screen = Constants.SCREENS.CHOOSE_TEACHER;
                break;
            case Constants.TYPE_STUDENT:
                screen = Constants.SCREENS.CHOOSE_GROUP;
        }

        return screen;
    }
}
