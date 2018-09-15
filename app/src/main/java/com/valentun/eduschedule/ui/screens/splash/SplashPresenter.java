package com.valentun.eduschedule.ui.screens.splash;

import android.annotation.SuppressLint;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.BuildConfig;
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
@SuppressLint("CheckResult")
public class SplashPresenter extends MvpPresenter<SplashView> {

    private static final int SCHOOL_USE_CACHED = -1;
    @Inject
    IRepository repository;
    @Inject
    SettingsManager manager;
    @Inject
    Router router;

    public SplashPresenter() {
        initDagger();
    }

    public void loadInfo(boolean forceUpdate, String screenKey, NamedEntity data) {
        repository.loadVersionInfo()
                .subscribe(versionInfo -> {
                    if (versionInfo.getVersion() > BuildConfig.VERSION_CODE) {
                        getViewState().showUpdateDialog();
                    } else {
                        loadSchool(forceUpdate, screenKey, data);
                    }
                }, error -> loadSchool(forceUpdate, screenKey, data));
    }

    public void loadSchool(boolean forceUpdate, String screenKey, NamedEntity data) {
        if (repository.isSchoolChosen()) {
            getSchoolData(repository.getSchoolId(), forceUpdate, screenKey, data);

        } else {
            router.replaceScreen(Constants.SCREENS.SCHOOL_SELECTOR);
        }
    }

    public void retry() {
        getSchoolData(repository.getSchoolId(), false, Constants.SCREENS.MAIN, null);
    }

    public void exit() {
        router.finishChain();
    }

    public void useCache() {
        getSchoolData(SCHOOL_USE_CACHED, false, Constants.SCREENS.MAIN, null);
    }

    private void getSchoolData(int schoolId, boolean forceUpdate, String screenKey, NamedEntity data) {
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
            if (data != null) {
                router.replaceScreen(screenKey, data);
            } else {
                router.replaceScreen(screenKey);
            }
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
