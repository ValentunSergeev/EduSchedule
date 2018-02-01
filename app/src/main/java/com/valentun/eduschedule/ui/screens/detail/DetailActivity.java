package com.valentun.eduschedule.ui.screens.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ActivityDetailBinding;
import com.valentun.eduschedule.ui.common.BaseActivity;
import com.valentun.eduschedule.ui.screens.detail.detail_group.WeekGroupPageAdapter;
import com.valentun.eduschedule.ui.screens.detail.detail_teacher.WeekTeacherPageAdapter;
import com.valentun.eduschedule.ui.screens.splash.SplashActivity;
import com.valentun.eduschedule.utils.DateUtils;
import com.valentun.parser.pojo.NamedEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.SupportAppNavigator;


public class DetailActivity extends BaseActivity {
    public static final String EXTRA_TYPE = "DetailActivity.EXTRA_TYPE";
    @Inject
    NavigatorHolder navigatorHolder;
    private ActivityDetailBinding binding;
    private ActionBar actionBar;
    private Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.INSTANCE.getAppComponent().inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        initToolbar();
        initPager();

        navigator = new DetailNavigator(this);
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
            transition.add(getType());
            transition.add(getObjectId());
            transition.add(getObjectName());

            intent.putStringArrayListExtra(SplashActivity.SCREEN_DETAIL_RETURN_KEY, transition);

            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
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
            case Constants.SCREENS.TEACHER_DETAIL:
                adapter = new WeekTeacherPageAdapter(getObjectId(), this);
                break;
            case Constants.SCREENS.GROUP_DETAIL:
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

    private String getObjectId() {
        return getIntent().getStringExtra(Intent.EXTRA_TEXT);
    }

    private String getObjectName() {
        return getIntent().getStringExtra(Intent.EXTRA_TITLE);
    }

    private String getType() {
        return getIntent().getStringExtra(EXTRA_TYPE);
    }

    private class DetailNavigator extends SupportAppNavigator {
        private DetailNavigator(FragmentActivity activity) {
            super(activity, 0);
        }

        @Override
        protected Intent createActivityIntent(String screenKey, Object data) {
            Intent intent;
            NamedEntity extras = (NamedEntity) data;
            if (screenKey.equals(Constants.SCREENS.GROUP_DETAIL) ||
                    screenKey.equals(Constants.SCREENS.TEACHER_DETAIL)) {
                intent = new Intent(DetailActivity.this, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_TYPE, screenKey);
                intent.putExtra(Intent.EXTRA_TEXT, extras.getId());
                intent.putExtra(Intent.EXTRA_TITLE, extras.getName());
            } else {
                throw new IllegalArgumentException("Unknown screen key");
            }

            return intent;
        }

        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            return null;
        }
    }
}
