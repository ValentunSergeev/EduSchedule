package com.valentun.eduschedule.ui.screens.groups;


import com.arellomobile.mvp.MvpView;
import com.valentun.parser.pojo.Group;

import java.util.List;

public interface GroupsView extends MvpView {
    void showGroups(List<Group> groups);
    void showProgress();
}
