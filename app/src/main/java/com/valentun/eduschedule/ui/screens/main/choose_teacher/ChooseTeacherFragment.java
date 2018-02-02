package com.valentun.eduschedule.ui.screens.main.choose_teacher;

import android.support.v7.widget.RecyclerView;
import android.widget.Filterable;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.ui.common.fragments.RecyclerViewFragment;
import com.valentun.eduschedule.ui.common.views.ListView;
import com.valentun.eduschedule.ui.screens.main.teachers.TeachersAdapter;
import com.valentun.eduschedule.utils.UIUtils;
import com.valentun.parser.pojo.NamedEntity;
import com.valentun.parser.pojo.Teacher;

import java.util.List;

public class ChooseTeacherFragment extends RecyclerViewFragment<Teacher>
        implements ListView<Teacher>, TeachersAdapter.EventHandler {

    @InjectPresenter
    ChooseTeacherPresenter presenter;

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle(R.string.select_teacher);
    }

    @Override
    protected Filterable getFilterable() {
        return () -> presenter.filter;
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Teacher> data) {
        return new TeachersAdapter(data, this);
    }

    @Override
    protected String getPlaceholderText() {
        return getString(R.string.list_empty_placeholder);
    }

    @Override
    protected void retryClicked() {
        presenter.getData();
    }

    @Override
    public void itemClicked(NamedEntity item) {
        UIUtils.hideKeyboard(getActivity());
        presenter.itemClicked(item);
    }
}