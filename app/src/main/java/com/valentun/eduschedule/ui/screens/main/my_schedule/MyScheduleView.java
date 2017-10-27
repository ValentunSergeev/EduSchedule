package com.valentun.eduschedule.ui.screens.main.my_schedule;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.valentun.parser.pojo.Group;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@StateStrategyType(AddToEndSingleStrategy.class)
public interface MyScheduleView extends MvpView{
    void showGroupSelector();
    void showMySchedule(String groupId);

    void showGroups(List<Group> groups);
}
