package com.valentun.eduschedule.ui.screens.main.my_schedule;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import com.valentun.eduschedule.utils.DateUtils;

public class MyScheduleFragment extends MvpAppCompatFragment
        implements MyScheduleView {

    ScreenMyScheduleBinding binding;

    @InjectPresenter
    MySchedulePresenter presenter;

    private IBarView barView;
    private Activity activity;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        barView = (IBarView) getActivity();
        activity = getActivity();

        binding = ScreenMyScheduleBinding.inflate(inflater, container, false);
        tabLayout = (TabLayout) inflater.inflate(R.layout.tab_layout, barView.getToolbarContainer(), false);
        barView.getToolbarContainer().addView(tabLayout);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.detailPager.setOffscreenPageLimit(Constants.DAY_NUMBER);
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
    public void showMySchedule(String groupId) {
        activity.setTitle(getString(R.string.my_schedule));

        binding.detailPager.setAdapter(new WeekGroupPageAdapter(groupId, getChildFragmentManager()));
        tabLayout.setupWithViewPager(binding.detailPager);
        binding.detailPager.setCurrentItem(DateUtils.ViewPagerUtils.getCurrentDayPosition());
    }
}
