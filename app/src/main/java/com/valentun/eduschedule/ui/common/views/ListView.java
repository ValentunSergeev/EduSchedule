package com.valentun.eduschedule.ui.common.views;


import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ListView<T> extends MvpView {
    void showData(List<T> items);
    void showPlaceholder();
    void showProgress();
    void showError(@StringRes int errorRes);
}
