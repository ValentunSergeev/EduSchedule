package com.valentun.eduschedule;


import android.support.annotation.StringDef;

public class Constants {
    public static final String BASE_URL = "https://eduschedule.herokuapp.com";

    public static final long MAX_TIMEOUT = 10_000;
    public static final int DAY_NUMBER = 6;
    public static final long SEARCH_DELAY = 300;
    public static final String NOTIFICATION_CHANNEL = "Eduschedule";
    public static final int NOTIFICATION_DELAY = 5; // 5 seconds
    public static final long[] VIBRATION_PATTERN = {0, 500, 500, 500};
    public static final long BACK_MODE_TIME = 1000; // one second

    public static final String TYPE_STUDENT = "STUDENT";
    public static final String TYPE_TEACHER = "TEACHER";
    public static final String TYPE_NONE = "NONE";
    public static final String WALLET_CLIP_LABEL = "Wallet";

    public static final String WALLET_VALUE = "410016467298531";

    public static class SCREENS {
        public static final String GROUPS_LIST = "GROUPS_LIST";
        public static final String GROUP_DETAIL = "GROUP_DETAIL";
        public static final String TEACHER_DETAIL = "TEACHER_DETAIL";
        public static final String TEACHERS_LIST = "TEACHERS_LIST";

        public static final String MY_SCHEDULE = "MY_SCHEDULE";
        public static final String CHOOSE_GROUP = "CHOOSE_GROUP";
        public static final String CHOOSE_TEACHER = "CHOOSE_TEACHER";

        public static final String INFO = "INFO";

        public static final String SCHOOL_SELECTOR = "SCHOOL_SELECTOR";
        public static final String MAIN = "MAIN";
        public static final String SPLASH = "SPLASH";
        public static final String FORCE_REFRESH = "FORCE_REFRESH";

        public static final String SETTINGS = "SETTINGS";

        public static final String SCHEDULE_TYPE_CHOOSER = "SCHEDULE_TYPE_CHOOSER";
    }

    public static class JOBS {
        public static final String CHECK_SCHEDULE = "CHECK_SCHEDULE";

        @StringDef({CHECK_SCHEDULE})
        public @interface JobsDef{}
    }

    public static class SETTINGS {
        public static final String SETTINGS_AUTO_CACHE = "auto_use_cache";
        public static final String SETTINGS_NOTIFICATIONS = "notifications";
        public static final String SETTINGS_SCHEDULE_TYPE = "schedule_type";
    }
}
