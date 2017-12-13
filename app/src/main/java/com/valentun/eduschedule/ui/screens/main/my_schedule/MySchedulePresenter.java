package com.valentun.eduschedule.ui.screens.main.my_schedule;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.di.AppComponent;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class MySchedulePresenter extends MvpPresenter<MyScheduleView> {
    @Inject
    IRepository repository;

    @Inject
    Router router;

    public MySchedulePresenter() {
        initDagger();

        if (repository.isGroupChosen()) {
            getViewState().showMySchedule(repository.getGroupId());
        } else {
            openGroupSelector();
        }
    }

    // ======= region MySchedulePresenter =======

    void changeGroupClicked() {
        repository.clearGroupId();

        openGroupSelector();
    }

    // end

    // ======= region private methods =======

    private void openGroupSelector() {
        router.navigateTo(Constants.SCREENS.CHOOSE_GROUP);
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }

    // end
}
