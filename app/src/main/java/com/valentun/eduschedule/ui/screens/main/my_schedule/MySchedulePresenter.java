package com.valentun.eduschedule.ui.screens.main.my_schedule;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.Repository;
import com.valentun.eduschedule.di.AppComponent;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class MySchedulePresenter extends MvpPresenter<MyScheduleView> {
    @Inject
    Repository repository;

    public MySchedulePresenter() {
        initDagger();

        if (repository.isGroupChosen()) {
            getViewState().showMySchedule(repository.getGroupId());
        } else {
            getViewState().showGroupSelector();
        }
    }

    // ======= region MySchedulePresenter =======

    void loadGroups() {
        repository.getGroups()
                .subscribe(groups -> {
                    getViewState().showGroups(groups);
                });
    }

    void groupSelected(String groupId) {
        repository.setGroupId(groupId);

        getViewState().showMySchedule(groupId);
    }

    void changeGroupClicked() {
        repository.clearGroupId();

        getViewState().showGroupSelector();
    }
    
    // end

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }
}
