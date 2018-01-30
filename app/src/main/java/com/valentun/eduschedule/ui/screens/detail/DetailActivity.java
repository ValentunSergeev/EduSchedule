package com.valentun.eduschedule.ui.screens.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ActivityDetailBinding;
import com.valentun.eduschedule.ui.common.BaseActivity;
import com.valentun.eduschedule.ui.screens.detail.detail_group.WeekGroupPageAdapter;
import com.valentun.eduschedule.ui.screens.detail.detail_teacher.WeekTeacherPageAdapter;
import com.valentun.eduschedule.ui.screens.splash.SplashActivity;
import com.valentun.eduschedule.utils.DateUtils;

import java.util.ArrayList;


public class DetailActivity extends BaseActivity {
    private ActivityDetailBinding binding;
    private ActionBar actionBar;

    public static final int TYPE_TEACHER = 0;
    public static final int TYPE_GROUP = 1;

    public static final String EXTRA_TYPE = "DetailActivity.EXTRA_TYPE";

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
            Intent intent = buildNewTaskIntent(SplashActivity.class);
            intent.putExtra(SplashActivity.EXTRA_FORCE_UPDATE, true);

            ArrayList<String> transition = new ArrayList<>();
            transition.add(getScreenReturnKey());
            transition.add(getObjectId());
            transition.add(getObjectName());

            intent.putStringArrayListExtra(SplashActivity.SCREEN_DETAIL_RETURN_KEY, transition);

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

        setTitle(getObjectName());
    }

    private void initPager() {
        FragmentStatePagerAdapter adapter;

        switch (getType()) {
            case TYPE_TEACHER:
                adapter = new WeekTeacherPageAdapter(getObjectId(), this);
                break;
            case TYPE_GROUP:
                adapter = new WeekGroupPageAdapter(getObjectId(), this);
                break;
            default:
                throw new IllegalArgumentException("Unknown detail type");
        }

        binding.detailPager.setAdapter(adapter);
        binding.detailPager.setOffscreenPageLimit(Constants.DAY_NUMBER);

        binding.tabLayout.setupWithViewPager(binding.detailPager);

        binding.detailPager.setCurrentItem(DateUtils.ViewPagerUtils.getCurrentDayPosition());
    }

    private String getScreenReturnKey() {
        String key;

        switch (getType()) {
            case TYPE_GROUP:
                key = Constants.SCREENS.GROUP_DETAIL;
                break;
            case TYPE_TEACHER:
                key = Constants.SCREENS.TEACHER_DETAIL;
                break;
            default:
                throw new IllegalArgumentException("Unknown detail type");
        }

        return key;
    }

    private String getObjectId() {
        return getIntent().getStringExtra(Intent.EXTRA_TEXT);
    }

    private String getObjectName() {
        return getIntent().getStringExtra(Intent.EXTRA_TITLE);
    }

    private int getType() {
        return getIntent().getIntExtra(EXTRA_TYPE, TYPE_GROUP);
    }
}
