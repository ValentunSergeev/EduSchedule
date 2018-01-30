package com.valentun.eduschedule.ui.screens.detail.detail_group;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.data.network.ErrorHandler;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.ui.common.views.ListView;
import com.valentun.parser.pojo.Lesson;

import java.util.List;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class DayGroupPresenter extends MvpPresenter<ListView<Lesson>> {
    @Inject
    IRepository repository;

    private List<Lesson> lessons;

    private String groupId;
    private int dayNumber;

    public DayGroupPresenter(String groupId, int dayNumber) {
        initDagger();

        this.groupId = groupId;
        this.dayNumber = dayNumber;

        getViewState().showProgress();
    }

    public void getData() {
        if (lessons == null) {
            repository.getGroupSchedule(groupId, dayNumber)
                    .subscribe(lessons -> {
                        this.lessons = lessons;

                        showData(lessons);
                    }, error -> {
                        getViewState().showError(ErrorHandler.getErrorMessage(error));
                    });
        } else {
            showData(lessons);
        }
    }

    private void showData(List<Lesson> lessons) {
        if (lessons.size() > 0)
            getViewState().showData(lessons);
        else
            getViewState().showPlaceholder();
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }
}
