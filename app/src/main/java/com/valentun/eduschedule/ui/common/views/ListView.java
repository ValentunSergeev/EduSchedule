package com.valentun.eduschedule.ui.common.views;


import com.arellomobile.mvp.MvpView;

import java.util.List;

public interface ListView<T> extends MvpView {
    void showData(List<T> items);
    void showProgress();
}
