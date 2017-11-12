package com.valentun.eduschedule.utils;

import android.support.annotation.Nullable;

import com.valentun.parser.pojo.Period;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static final int DAYS_IN_WEEK = 7;
    private static final String TIME_PATTERN = "HH:mm";

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

        public static boolean isPageCurrent(int position) {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            return day != Calendar.SUNDAY && convertWeekDayToPosition(day) == position;
        }
    }

    public static boolean isPeriodCurrentOrNext(Period period, @Nullable Period previous) {
        SimpleDateFormat format = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

        Date now = new Date(System.currentTimeMillis() % (24 * 1000 * 60 * 60));

        try {
            Date start = format.parse(period.getStartTime());
            Date end = format.parse(period.getEndTime());

            long nowTime = now.getTime();

            if (nowTime >= start.getTime() && nowTime <= end.getTime()) {
                return true;
            }

            if (previous != null) {
                Date previousEnd = format.parse(previous.getEndTime());

                if (now.after(previousEnd) && now.before(start)) {
                    return true;
                }
            } else {
                return now.before(start);
            }

            return false;
        } catch (ParseException e) {
            return false;
        }
    }
}
