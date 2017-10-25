package com.valentun.eduschedule.ui.screens.detail_teacher;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.utils.DateUtils;

class WeekTeacherPageAdapter extends FragmentStatePagerAdapter {
    private String teacherId;

    WeekTeacherPageAdapter(String teacherId, AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        this.teacherId = teacherId;
    }

    @Override
    public Fragment getItem(int position) {
        return DayTeacherFragment.newInstance(teacherId, position);
    }

    @Override
    public int getCount() {
        return Constants.DAY_NUMBER;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return DateUtils.ViewPagerUtils.getWeekDayNameByPosition(position);
    }
}
