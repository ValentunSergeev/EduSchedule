package com.valentun.eduschedule.utils;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class DateUtils {
    private static final int DAYS_IN_WEEK = 7;

    private static int convertPositionToWeekDay(int position) {
        switch (position) {
            case DAYS_IN_WEEK - 1:
                return 1;
            default:
                return position + 2;
        }
    }

    private static int convertWeekDayToPosition(int weekDay) {
        switch (weekDay) {
            case Calendar.SUNDAY:
                return DAYS_IN_WEEK - 1;
            default:
                return weekDay - 2;
        }
    }

    public static class ViewPagerUtils {
        public static String getWeekDayNameByPosition(int position) {
            int index = DateUtils.convertPositionToWeekDay(position);
            return DateFormatSymbols.getInstance().getWeekdays()[index];
        }

        public static int getCurrentDayPosition() {
            int weekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

            if (weekDay == Calendar.SUNDAY)
                return 0;
            else
                return DateUtils.convertWeekDayToPosition(weekDay);
        }
    }
}
