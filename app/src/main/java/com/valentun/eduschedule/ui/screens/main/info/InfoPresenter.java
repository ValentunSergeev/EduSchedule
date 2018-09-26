package com.valentun.eduschedule.ui.screens.main.info;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.di.AppComponent;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class InfoPresenter extends MvpPresenter<MvpView> {
    @Inject
    Router router;

    public InfoPresenter() {
        initDagger();
    }

    public void copyClicked() {
        router.showSystemMessage(MyApplication.INSTANCE.getString(R.string.wallet_copied));
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }
}
