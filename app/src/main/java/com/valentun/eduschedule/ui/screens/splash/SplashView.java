package com.valentun.eduschedule.ui.screens.splash;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface SplashView extends MvpView {
    void showError(@StringRes int stringRes, boolean displayUseCache);

    void showUpdateDialog();
}
