package com.valentun.eduschedule.ui.screens.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.ui.screens.main.MainActivity;
import com.valentun.eduschedule.ui.screens.school_selector.SchoolSelectActivity;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.SupportAppNavigator;


public class SplashActivity extends MvpAppCompatActivity implements MvpView {
    @Inject
    NavigatorHolder navigatorHolder;

    @InjectPresenter
    SplashPresenter presenter;

    private Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.INSTANCE.getAppComponent().inject(this);

        super.onCreate(savedInstanceState);

        navigator = new SplashNavigator(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    private class SplashNavigator extends SupportAppNavigator {

        private SplashNavigator(FragmentActivity activity) {
            super(activity, 0);
        }

        @Override
        protected Intent createActivityIntent(String screenKey, Object data) {
            Class activityClass = null;

            switch (screenKey) {
                case Constants.SCREENS.SCHOOL_SELECTOR:
                    activityClass = SchoolSelectActivity.class;
                    break;
                case Constants.SCREENS.MAIN:
                    activityClass = MainActivity.class;
            }

            return new Intent(SplashActivity.this, activityClass);
        }

        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            return null;
        }
    }
}
