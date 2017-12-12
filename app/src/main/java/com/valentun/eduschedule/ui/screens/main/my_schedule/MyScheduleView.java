package com.valentun.eduschedule.ui.screens.main.my_schedule;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.valentun.parser.pojo.Group;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@StateStrategyType(SingleStateStrategy.class)
public interface MyScheduleView extends MvpView{
    void showGroupSelector();
    void showMySchedule(String groupId);

    void showGroups(List<Group> groups);

    void showGroupLoadingError(@StringRes int errorMessage);
}
