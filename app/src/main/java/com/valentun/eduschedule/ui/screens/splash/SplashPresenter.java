package com.valentun.eduschedule.ui.screens.splash;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.data.network.ErrorHandler;
import com.valentun.eduschedule.data.persistance.SettingsManager;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.parser.pojo.NamedEntity;
import com.valentun.parser.pojo.School;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.terrakok.cicerone.Router;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class SplashPresenter extends MvpPresenter<SplashView> {

    private static final int SCHOOL_USE_CACHED = -1;
    @Inject
    IRepository repository;
    @Inject
    SettingsManager manager;
    @Inject Router router;

    public SplashPresenter() {
        initDagger();
    }

    public void loadSchool(boolean forceUpdate, String screenKey, NamedEntity data) {
        if (repository.isSchoolChosen()) {
            getSchoolData(repository.getSchoolId(), forceUpdate);
            if (data != null) {
                router.replaceScreen(screenKey, data);
            } else {
                router.replaceScreen(screenKey);
            }
        } else {
            router.replaceScreen(Constants.SCREENS.SCHOOL_SELECTOR);
        }
    }

    public void retry() {
        getSchoolData(repository.getSchoolId(), false);
    }

    public void exit() {
        router.exit();
    }

    public void useCache() {
        getSchoolData(SCHOOL_USE_CACHED, false);
    }

    private void getSchoolData(int schoolId, boolean forceUpdate) {
        Observable<School> observable;

        if (schoolId == SCHOOL_USE_CACHED) {
            observable = repository.getCachedSchool();
        } else {
            observable = repository.getSchool(schoolId, forceUpdate);
        }

        observable.subscribe(school -> {
            if (repository.isCachedSchedule())
                router.showSystemMessage(MyApplication.INSTANCE.getString(
                        R.string.cached_version_used,
                        repository.getCachedTime()
                ));

        }, error -> {
            boolean isCacheAvailable = repository.isCacheAvailable();

            if (isCacheAvailable && manager.isAutoUseCacheEnabled()) {
                useCache();
            } else {
                getViewState().showError(ErrorHandler.getErrorMessage(error), isCacheAvailable);
            }
        });
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }
}
