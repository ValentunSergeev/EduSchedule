package com.valentun.eduschedule;


public class Constants {
    public static final String BASE_URL = "https://eduschedule.herokuapp.com/";

    public static final long MAX_TIMEOUT = 10_000;
    public static final int DAY_NUMBER = 6;
    public static final long SEARCH_DELAY = 300;

    public static class SCREENS {
        public static final String GROUPS_LIST = "GROUPS_LIST";
        public static final String GROUP_DETAIL = "GROUP_DETAIL";
        public static final String TEACHER_DETAIL = "TEACHER_DETAIL";
        public static final String TEACHERS_LIST = "TEACHERS_LIST";

        public static final String MY_SCHEDULE = "MY_SCHEDULE";

        public static final String SCHOOL_SELECTOR = "SCHOOL_SELECTOR";
        public static final String MAIN = "MAIN";
        public static final String SPLASH = "SPLASH";
    }
}
