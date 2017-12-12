package com.valentun.eduschedule.ui.screens.main.my_schedule;

import android.widget.Filter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.data.network.ErrorHandler;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.ui.common.BaseFilter;
import com.valentun.parser.pojo.Group;

import java.util.List;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class MySchedulePresenter extends MvpPresenter<MyScheduleView> {
    @Inject
    IRepository repository;

    public MySchedulePresenter() {
        initDagger();

        if (repository.isGroupChosen()) {
            getViewState().showMySchedule(repository.getGroupId());
        } else {
            getViewState().showGroupSelector();
        }
    }

    Filter filter = new BaseFilter<Group>() {
        @Override
        protected void showResult(List<Group> result) {
            getViewState().showGroups(result);
        }

        @Override
        protected List<Group> findResult(CharSequence query) {
            return repository.findGroups(query);
        }
    };

    // ======= region MySchedulePresenter =======

    void loadGroups() {
        repository.getGroups()
                .subscribe(groups -> {
                    getViewState().showGroups(groups);
                }, error -> {
                    getViewState().showGroupLoadingError(ErrorHandler.getErrorMessage(error));
                });
    }

    void groupSelected(String groupId) {
        repository.setGroupId(groupId);

        getViewState().showMySchedule(groupId);
    }

    void changeGroupClicked() {
        repository.clearGroupId();

        getViewState().showGroupSelector();
    }
    
    // end

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }
}
