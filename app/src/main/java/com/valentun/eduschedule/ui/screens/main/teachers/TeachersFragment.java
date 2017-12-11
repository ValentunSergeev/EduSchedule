package com.valentun.eduschedule.ui.screens.main.teachers;

import android.support.v7.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Filterable;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.ui.common.fragments.RecyclerViewFragment;
import com.valentun.eduschedule.ui.common.views.ListView;
import com.valentun.parser.pojo.NamedEntity;
import com.valentun.parser.pojo.Teacher;

import java.util.List;

public class TeachersFragment extends RecyclerViewFragment<Teacher>
        implements ListView<Teacher>, TeachersAdapter.EventHandler {
    @InjectPresenter
    TeachersPresenter presenter;

    @Override
    protected Filterable getFilterable() {
        return () -> presenter.filter;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle(R.string.teachers);
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Teacher> data) {
        return new TeachersAdapter(data, this);
    }

    @Override
    protected String getPlaceholderText() {
        return getString(R.string.no_teachers);
    }

    @Override
    protected void retryClicked() {
        presenter.getData();
    }

    @Override
    public void itemClicked(NamedEntity item) {
        presenter.itemClicked(item);
    }
}
