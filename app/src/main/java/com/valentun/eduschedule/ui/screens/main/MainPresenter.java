package com.valentun.eduschedule.ui.screens.main;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.di.AppComponent;

import javax.inject.Inject;

@InjectViewState
public class MainPresenter extends MvpPresenter<MvpView> {
    @Inject
    IRepository repository;

    public MainPresenter() {
        initDagger();
    }

    void schoolChangeClicked() {
        repository.clearSchoolId();
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }
}
