package com.valentun.eduschedule.ui.screens.main.groups;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.data.network.ErrorHandler;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.ui.common.views.ListView;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.NamedEntity;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class GroupsPresenter extends MvpPresenter<ListView<Group>> {
    @Inject
    Router router;
    @Inject
    IRepository repository;

    public GroupsPresenter() {
        initDagger();

        getViewState().showProgress();
        getData();
    }

    // ======= region GroupsPresenter =======

    void getData() {
        repository.getGroups()
                .subscribe(groups -> {
                    getViewState().showData(groups);
                }, error -> {
                    getViewState().showError(ErrorHandler.getErrorMessage(error));
                });
    }

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

    //endregion
}
