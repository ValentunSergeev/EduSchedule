package com.valentun.eduschedule.ui.screens.groups;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.Repository;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.parser.pojo.NamedEntity;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class GroupsPresenter extends MvpPresenter<GroupsView> {
    @Inject
    Router router;
    @Inject
    Repository repository;

    public GroupsPresenter() {
        initDagger();

        getViewState().showProgress();
        getData();
    }

    // ======= region GroupsPresenter =======

    void itemClicked(NamedEntity group) {
        router.showSystemMessage(group.getName());
    }

    //endregion

    // ======= region private methods =======

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }

    private void getData() {
        repository.getSchool()
                .subscribe(school -> {
                    getViewState().showGroups(school.getGroups());
                });
    }

    //endregion
}
