package com.valentun.eduschedule.ui.screens.main.choose_group;

import android.widget.Filter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.data.network.ErrorHandler;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.ui.common.BaseFilter;
import com.valentun.eduschedule.ui.common.views.ListView;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.NamedEntity;

import java.util.List;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@InjectViewState
public class ChooseGroupPresenter extends MvpPresenter<ListView<Group>> {
    @Inject
    Router router;
    @Inject
    IRepository repository;

    Filter filter = new BaseFilter<Group>() {
        @Override
        protected void showResult(List<Group> result) {
            getViewState().showData(result);
        }

        @Override
        protected List<Group> findResult(CharSequence query) {
            return repository.findGroups(query);
        }
    };

    public ChooseGroupPresenter() {
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
        repository.setGroupId(group.getId());
        router.navigateTo(Constants.SCREENS.MY_SCHEDULE, group);
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
