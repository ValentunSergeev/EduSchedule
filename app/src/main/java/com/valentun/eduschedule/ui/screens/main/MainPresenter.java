package com.valentun.eduschedule.ui.screens.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.ui.common.views.MainView;

import javax.inject.Inject;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    @Inject
    IRepository repository;

    public MainPresenter() {
        initDagger();
    }

    void schoolChangeClicked() {
        repository.clearSchoolId();
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
