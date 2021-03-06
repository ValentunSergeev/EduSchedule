package com.valentun.eduschedule.ui.screens.main.my_schedule;

import com.arellomobile.mvp.MvpView;

@SuppressWarnings("WeakerAccess")
public interface MyScheduleView extends MvpView {
    void showMySchedule(String objectId, String scheduleType);
    void showObjectName(String name);
}