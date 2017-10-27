package com.valentun.eduschedule.ui.screens.detail_group;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ActivityDetailBinding;
import com.valentun.eduschedule.utils.DateUtils;

public class DetailGroupActivity extends MvpAppCompatActivity {

    private ActivityDetailBinding binding;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        initToolbar();

        initPager();
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);

        actionBar = getSupportActionBar();

        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(getGroupName());
    }

    private void initPager() {
        binding.detailPager.setAdapter(new WeekGroupPageAdapter(getGroupId(), this));
        binding.detailPager.setOffscreenPageLimit(Constants.DAY_NUMBER);

        binding.tabLayout.setupWithViewPager(binding.detailPager);

        binding.detailPager.setCurrentItem(DateUtils.ViewPagerUtils.getCurrentDayPosition());

    }

    private String getGroupId() {
        return getIntent().getStringExtra(Intent.EXTRA_TEXT);
    }

    private String getGroupName() {
        return getIntent().getStringExtra(Intent.EXTRA_TITLE);
    }
}
