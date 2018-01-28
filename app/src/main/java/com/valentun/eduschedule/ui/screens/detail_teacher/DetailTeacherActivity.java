package com.valentun.eduschedule.ui.screens.detail_teacher;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ActivityDetailBinding;
import com.valentun.eduschedule.ui.screens.splash.SplashActivity;
import com.valentun.eduschedule.utils.DateUtils;

public class DetailTeacherActivity extends MvpAppCompatActivity {
    private ActivityDetailBinding binding;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        initToolbar();
        initPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.force_refresh) {
            Intent intent = new Intent(DetailTeacherActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(SplashActivity.EXTRA_FORCE_UPDATE, true);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);

        actionBar = getSupportActionBar();

        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle(getTeacherName());
    }

    private void initPager() {
        binding.detailPager.setAdapter(new WeekTeacherPageAdapter(getTeacherId(), this));
        binding.detailPager.setOffscreenPageLimit(Constants.DAY_NUMBER);

        binding.tabLayout.setupWithViewPager(binding.detailPager);

        binding.detailPager.setCurrentItem(DateUtils.ViewPagerUtils.getCurrentDayPosition());

    }

    private String getTeacherId() {
        return getIntent().getStringExtra(Intent.EXTRA_TEXT);
    }

    private String getTeacherName() {
        return getIntent().getStringExtra(Intent.EXTRA_TITLE);
    }
}