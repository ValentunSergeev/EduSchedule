package com.valentun.eduschedule.ui.screens.detail_group;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.utils.DateUtils;


class WeekPageAdapter extends FragmentStatePagerAdapter {
    private String groupId;

    WeekPageAdapter(String groupId, AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        this.groupId = groupId;
    }

    @Override
    public Fragment getItem(int position) {
        return DayFragment.newInstance(groupId, position);
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
