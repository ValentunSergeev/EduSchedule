package com.valentun.eduschedule.ui.screens.splash;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;

public interface SplashView extends MvpView {
    void showError(@StringRes int stringRes);
}
