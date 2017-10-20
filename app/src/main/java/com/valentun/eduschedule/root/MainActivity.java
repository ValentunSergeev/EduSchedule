package com.valentun.eduschedule.root;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.valentun.eduschedule.Constants.SCREENS;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ActivityMainBinding;
import com.valentun.eduschedule.screens.groups.GroupsFragment;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;
import ru.terrakok.cicerone.commands.Replace;

public class MainActivity extends MvpAppCompatActivity {

    @Inject
    NavigatorHolder navigatorHolder;

    private Navigator navigator;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.INSTANCE.getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        navigator = new MainNavigator(getSupportFragmentManager(), R.id.main_container);

        if (savedInstanceState == null)
            navigator.applyCommand(new Replace(SCREENS.GROUPS_LIST, null));
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

    private class MainNavigator extends SupportFragmentNavigator {
        MainNavigator(FragmentManager fragmentManager, int containerId) {
            super(fragmentManager, containerId);
        }

        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            switch (screenKey) {
                case SCREENS.GROUPS_LIST:
                    return new GroupsFragment();
                default:
                    throw new UnsupportedOperationException("Unknown screen key");
            }
        }

        @Override
        protected void showSystemMessage(String message) {
            Snackbar.make(binding.mainContainer, message, Snackbar.LENGTH_LONG)
                    .show();
        }

        @Override
        protected void exit() {
            finish();
        }
    }
}
