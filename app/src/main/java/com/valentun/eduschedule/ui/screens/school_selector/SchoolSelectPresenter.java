package com.valentun.eduschedule.ui.screens.school_selector;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.data.dto.SchoolInfo;
import com.valentun.eduschedule.data.network.ErrorHandler;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.ui.common.views.ListView;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class SchoolSelectPresenter extends MvpPresenter<ListView<SchoolInfo>> {
    @Inject
    Router router;

    @Inject
    IRepository repository;

    public SchoolSelectPresenter() {
        initDagger();

        getViewState().showProgress();
        getData();
    }

    void schoolSelected(int schoolId) {
        repository.setSchoolId(schoolId);

        router.replaceScreen(Constants.SCREENS.SPLASH);
    }

    public void getData() {
        repository.getSchools().subscribe(schools -> {
            getViewState().showData(schools);
        }, error -> {
            getViewState().showError(ErrorHandler.getErrorMessage(error));
        });
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }
}
