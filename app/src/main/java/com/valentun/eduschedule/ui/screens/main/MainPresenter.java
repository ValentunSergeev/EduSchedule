package com.valentun.eduschedule.ui.screens.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.di.AppComponent;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    @Inject
    IRepository repository;
    @Inject
    Router router;
    public MainPresenter() {
        initDagger();
    }

    void schoolChangeClicked() {
        repository.clearSchoolId();
    }

    void forceRefreshClicked() {
        router.replaceScreen(Constants.SCREENS.FORCE_REFRESH);
    }

    void loadName() {
        repository.getSchoolName().subscribe(name -> getViewState().showName(name));
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }
}
