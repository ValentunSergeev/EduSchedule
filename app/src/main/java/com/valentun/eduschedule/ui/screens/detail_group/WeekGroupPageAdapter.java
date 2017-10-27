package com.valentun.eduschedule.ui.screens.detail_group;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.utils.DateUtils;


public class WeekGroupPageAdapter extends FragmentStatePagerAdapter {
    private String groupId;

    WeekGroupPageAdapter(String groupId, AppCompatActivity activity) {
        super(activity.getSupportFragmentManager());
        this.groupId = groupId;
    }

    public WeekGroupPageAdapter(String groupId, FragmentManager manager) {
        super(manager);
        this.groupId = groupId;
    }

    @Override
    public Fragment getItem(int position) {
        return DayGroupFragment.newInstance(groupId, position);
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
