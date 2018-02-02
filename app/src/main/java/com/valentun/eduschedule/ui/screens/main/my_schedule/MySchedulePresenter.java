package com.valentun.eduschedule.ui.screens.main.my_schedule;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.data.persistance.SettingsManager;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.utils.NavigationUtils;
import com.valentun.parser.pojo.NamedEntity;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class MySchedulePresenter extends MvpPresenter<MyScheduleView> {
    @Inject
    IRepository repository;

    @Inject
    Router router;

    @Inject
    SettingsManager settingsManager;

    private NamedEntity object;

    public MySchedulePresenter() {
        initDagger();

        if (!settingsManager.isTypeChosen()) {
            repository.clearMyScheduleObjectId();

            router.navigateTo(Constants.SCREENS.SCHEDULE_TYPE_CHOOSER);
        } else {
            if (repository.isObjectChosen()) {
                getViewState().showMySchedule(repository.getObjectId(), settingsManager.getPreferredScheduleType());

                if (object == null)
                    repository.getChosenScheduleObject()
                            .subscribe(object -> {
                                this.object = object;
                                getViewState().showObjectName(object.getName());
                            });
                else
                    getViewState().showObjectName(object.getName());
            } else {
                openScheduleSelector();
            }
        }
    }

    // ======= region MySchedulePresenter =======

    void changeGroupClicked() {
        repository.clearMyScheduleObjectId();

        openScheduleSelector();
    }

    // end

    // ======= region private methods =======

    private void openScheduleSelector() {
        String screen = NavigationUtils.getScreenKeyFromScheduleType(settingsManager.getPreferredScheduleType());
        router.navigateTo(screen);
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }

    // end
}
