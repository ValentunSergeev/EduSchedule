package com.valentun.eduschedule.ui.screens.detail_group;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.Repository;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.ui.common.views.ListView;
import com.valentun.parser.pojo.Lesson;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class DayGroupPresenter extends MvpPresenter<ListView<Lesson>> {
    @Inject
    Repository repository;

    public DayGroupPresenter(String groupId, int dayNumber) {
        initDagger();

        getViewState().showProgress();
        getData(groupId, dayNumber);
    }

    private void getData(String groupId, int dayNumber) {
        repository.getGroupSchedule(groupId, dayNumber)
                .subscribe(lessons -> {
                    getViewState().showData(lessons);
                });
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }
}
