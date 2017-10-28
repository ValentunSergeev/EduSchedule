package com.valentun.eduschedule.ui.screens.splash;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.di.AppComponent;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class SplashPresenter extends MvpPresenter<MvpView> {

    @Inject
    IRepository repository;

    @Inject Router router;

    public SplashPresenter() {
        initDagger();

        if (repository.isSchoolChosen()) {
            getSchoolData(repository.getSchoolId());
        } else {
            router.replaceScreen(Constants.SCREENS.SCHOOL_SELECTOR);
        }
    }

    private void getSchoolData(int schoolId) {
        repository.getSchool(schoolId).subscribe(school -> {
            router.replaceScreen(Constants.SCREENS.MAIN);
        }, error -> {
            router.showSystemMessage(error.getMessage());
        });
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }
}
