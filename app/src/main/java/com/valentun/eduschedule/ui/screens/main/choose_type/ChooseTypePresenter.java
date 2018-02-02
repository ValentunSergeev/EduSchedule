package com.valentun.eduschedule.ui.screens.main.choose_type;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.persistance.SettingsManager;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.utils.NavigationUtils;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class ChooseTypePresenter extends MvpPresenter<MvpView> {
    @Inject
    SettingsManager settingsManager;

    @Inject
    Router router;

    public ChooseTypePresenter() {
        initDagger();
    }

    void nextClicked(String scheduleType) {
        settingsManager.setPreferredScheduleType(scheduleType);

        router.navigateTo(NavigationUtils.getScreenKeyFromScheduleType(scheduleType));
    }

    // ======= region private methods =======

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }

    //endregion
}
