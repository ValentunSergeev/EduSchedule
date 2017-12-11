package com.valentun.eduschedule.ui.screens.main.teachers;

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
import com.valentun.parser.pojo.NamedEntity;
import com.valentun.parser.pojo.Teacher;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;

@SuppressWarnings("WeakerAccess")
@InjectViewState
public class TeachersPresenter extends MvpPresenter<ListView<Teacher>> {
    @Inject
    Router router;
    @Inject
    IRepository repository;

    Filter filter = new BaseFilter<Teacher>() {
        @Override
        protected void showResult(List<Teacher> result) {
            getViewState().showData(result);
        }

        @Override
        protected List<Teacher> findResult(CharSequence query) {
            return repository.findTeachers(query);
        }
    };

    public TeachersPresenter() {
        initDagger();
        getViewState().showProgress();
        getData();
    }

    void itemClicked(NamedEntity teacher) {
        router.navigateTo(Constants.SCREENS.TEACHER_DETAIL, teacher);
    }

    public void getData() {
        repository.getTeachers()
                .subscribe(teachers -> {
                    getViewState().showData(teachers);
                }, error -> {
                    getViewState().showError(ErrorHandler.getErrorMessage(error));
                });
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }
}
