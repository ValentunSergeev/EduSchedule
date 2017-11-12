package com.valentun.eduschedule.ui.screens.main.my_schedule;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ScreenMyScheduleBinding;
import com.valentun.eduschedule.ui.screens.detail_group.WeekGroupPageAdapter;
import com.valentun.eduschedule.ui.screens.main.IBarView;
import com.valentun.eduschedule.ui.screens.main.groups.GroupsAdapter;
import com.valentun.eduschedule.utils.DateUtils;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.NamedEntity;

import java.util.List;

public class MyScheduleFragment extends MvpAppCompatFragment
        implements MyScheduleView, GroupsAdapter.EventHandler {
    ScreenMyScheduleBinding binding;

    private IBarView barView;
    private Activity activity;

    private TabLayout tabLayout;

    @InjectPresenter
    MySchedulePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        barView = (IBarView) getActivity();
        activity = getActivity();

        binding = ScreenMyScheduleBinding.inflate(inflater, container, false);
        tabLayout = (TabLayout) inflater.inflate(R.layout.tab_layout, barView.getToolbarContainer(), false);
        barView.getToolbarContainer().addView(tabLayout);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.detailPager.setOffscreenPageLimit(Constants.DAY_NUMBER);
        binding.groupsSelector.setHasFixedSize(true);
        binding.groupsSelector.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_schedule_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.my_schedule_change_group) {
            presenter.changeGroupClicked();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        barView.getToolbarContainer().removeView(tabLayout);
    }

    @Override
    public void showGroupSelector() {
        tabLayout.setVisibility(View.GONE);
        binding.detailPager.setVisibility(View.GONE);
        binding.groupsSelector.setVisibility(View.GONE);

        showGroupsProgress();

        activity.setTitle(getString(R.string.group_selector_title));

        presenter.loadGroups();
    }

    @Override
    public void showMySchedule(String groupId) {
        binding.groupsSelector.setVisibility(View.GONE);
        binding.progress.setVisibility(View.GONE);

        binding.detailPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);

        activity.setTitle(getString(R.string.my_schedule));

        binding.detailPager.setAdapter(new WeekGroupPageAdapter(groupId, getChildFragmentManager()));
        tabLayout.setupWithViewPager(binding.detailPager);
        binding.detailPager.setCurrentItem(DateUtils.ViewPagerUtils.getCurrentDayPosition());
    }

    @Override
    public void showGroups(List<Group> groups) {
        binding.progress.setVisibility(View.GONE);

        binding.groupsSelector.setVisibility(View.VISIBLE);
        binding.groupsSelector.setAdapter(new GroupsAdapter(groups, this));
    }

    @Override
    public void showGroupLoadingError(@StringRes int errorMessage) {
        showGroupsProgress();

        Snackbar.make(binding.getRoot(), errorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, v -> presenter.loadGroups())
                .show();
    }

    @Override
    public void itemClicked(NamedEntity item) {
        presenter.groupSelected(item.getId());
    }

    private void showGroupsProgress() {
        binding.progress.setVisibility(View.VISIBLE);
    }
}
