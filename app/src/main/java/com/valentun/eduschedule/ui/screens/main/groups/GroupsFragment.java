package com.valentun.eduschedule.ui.screens.main.groups;

import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.widget.Filterable;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.ui.common.views.ListView;
import com.valentun.eduschedule.ui.common.fragments.RecyclerViewFragment;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.NamedEntity;

import java.util.List;


public class GroupsFragment extends RecyclerViewFragment<Group>
        implements ListView<Group>, GroupsAdapter.EventHandler {

    @InjectPresenter
    GroupsPresenter presenter;

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle(R.string.groups);
    }

    @Override
    protected Filterable getFilterable() {
        return () -> presenter.filter;
    }

    @Override
    protected int getFilterType() {
        return InputType.TYPE_CLASS_NUMBER;
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Group> data) {
        return new GroupsAdapter(data, this);
    }

    @Override
    protected String getPlaceholderText() {
        return getString(R.string.no_groups);
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
