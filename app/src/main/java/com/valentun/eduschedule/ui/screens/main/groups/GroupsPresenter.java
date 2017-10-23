package com.valentun.eduschedule.ui.screens.main.groups;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.Repository;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.ui.common.views.ListView;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.NamedEntity;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class GroupsPresenter extends MvpPresenter<ListView<Group>> {
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
        router.navigateTo(Constants.SCREENS.GROUP_DETAIL, group);
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
                    getViewState().showData(school.getGroups());
                });
    }

    //endregion
}
