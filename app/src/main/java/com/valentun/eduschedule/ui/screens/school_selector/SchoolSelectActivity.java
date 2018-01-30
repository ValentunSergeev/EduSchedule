package com.valentun.eduschedule.ui.screens.school_selector;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ActivitySchoolSelectBinding;
import com.valentun.eduschedule.ui.screens.splash.SplashActivity;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.SupportAppNavigator;

public class SchoolSelectActivity extends MvpAppCompatActivity {

    @Inject
    NavigatorHolder navigatorHolder;

    private Navigator navigator;

    private ActivitySchoolSelectBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.INSTANCE.getAppComponent().inject(this);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_school_select);

        setSupportActionBar(binding.toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, new SchoolSelectFragment())
                .commit();

        navigator = new SelectSchoolNavigator(this);
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
            Intent intent = new Intent(SchoolSelectActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(SplashActivity.EXTRA_FORCE_UPDATE, true);
            intent.putExtra(SplashActivity.SCREEN_RETURN_KEY, Constants.SCREENS.SCHOOL_SELECTOR);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    private class SelectSchoolNavigator extends SupportAppNavigator {

        private SelectSchoolNavigator(FragmentActivity activity) {
            super(activity, 0);
        }

        @Override
        protected Intent createActivityIntent(String screenKey, Object data) {
            Class activityClass = null;

            switch (screenKey) {
                case Constants.SCREENS.SPLASH:
                    activityClass = SplashActivity.class;
            }

            return new Intent(SchoolSelectActivity.this, activityClass);
        }

        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            return null;
        }
    }
}
