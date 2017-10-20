package com.valentun.eduschedule.screens.groups;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ScreenGroupsBinding;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.NamedEntity;

import java.util.List;


public class GroupsFragment extends MvpAppCompatFragment implements GroupsView, GroupsAdapter.EventHandler {
    private ScreenGroupsBinding binding;

    @InjectPresenter
    GroupsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.screen_groups, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        binding.groupsList.setHasFixedSize(true);
        binding.groupsList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void showGroups(List<Group> groups) {
        binding.groupsProgress.setVisibility(View.GONE);
        binding.groupsList.setAdapter(new GroupsAdapter(groups, this));
    }

    @Override
    public void showProgress() {
        binding.groupsList.setAdapter(null);
        binding.groupsProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void itemClicked(NamedEntity item) {
        presenter.itemClicked(item);
    }
}
