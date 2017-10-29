package com.valentun.eduschedule.ui.common.views;


import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ListView<T> extends MvpView {
    void showData(List<T> items);
    void showPlaceholder();
    void showProgress();
}
