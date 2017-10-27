package com.valentun.eduschedule.ui.screens.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.valentun.eduschedule.Constants.SCREENS;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ActivityMainBinding;
import com.valentun.eduschedule.ui.common.callbacks.BackButtonListener;
import com.valentun.eduschedule.ui.screens.detail_group.DetailGroupActivity;
import com.valentun.eduschedule.ui.screens.detail_teacher.DetailTeacherActivity;
import com.valentun.eduschedule.ui.screens.main.groups.GroupsFragment;
import com.valentun.eduschedule.ui.screens.main.my_schedule.MyScheduleFragment;
import com.valentun.eduschedule.ui.screens.main.teachers.TeachersFragment;
import com.valentun.parser.pojo.NamedEntity;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Forward;
import ru.terrakok.cicerone.commands.Replace;

public class MainActivity extends MvpAppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, IBarView {

    @Inject NavigatorHolder navigatorHolder;

    private Navigator navigator;

    private ActivityMainBinding binding;
    private ActionBar actionBar;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.INSTANCE.getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initToolBar();

        navigator = new MainNavigator(getSupportFragmentManager(), R.id.main_container);

        if (savedInstanceState == null) {
            binding.navView.setCheckedItem(R.id.nav_item_my_schedule);
            navigator.applyCommand(new Replace(SCREENS.MY_SCHEDULE, null));
        }
    }

    private void initToolBar() {
        setSupportActionBar(binding.toolbar);
        actionBar = getSupportActionBar();

        drawerToggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout,
                binding.toolbar,
                R.string.open_drawer,
                R.string.close_drawer);
        binding.drawerLayout.addDrawerListener(drawerToggle);
        binding.drawerLayout.setFitsSystemWindows(true);
        binding.navView.setNavigationItemSelectedListener(this);
        drawerToggle.syncState();
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String screen;

        switch (item.getItemId()) {
            case R.id.nav_item_groups:
                screen = SCREENS.GROUPS_LIST;
                break;
            case R.id.nav_item_teachers:
                screen = SCREENS.TEACHERS_LIST;
                break;
            case R.id.nav_item_my_schedule:
                screen = SCREENS.MY_SCHEDULE;
                break;
            default:
                screen = SCREENS.GROUPS_LIST;
        }

        navigator.applyCommand(new Replace(screen, null));
        binding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @SuppressWarnings("UnnecessaryReturnStatement")
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (fragment != null
                && fragment instanceof BackButtonListener
                && ((BackButtonListener) fragment).onBackPressed()) {
            return;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public AppBarLayout getToolbarContainer() {
        return binding.appbarLayout;
    }

    private class MainNavigator extends SupportFragmentNavigator {
        MainNavigator(FragmentManager fragmentManager, int containerId) {
            super(fragmentManager, containerId);
        }

        @Override
        public void applyCommand(Command command) {
            if (command instanceof Forward) {
                Forward forward = (Forward) command;
                if (forward.getScreenKey().equals(SCREENS.GROUP_DETAIL)) {
                    showDetail(forward);
                    return;
                }
                if (forward.getScreenKey().equals(SCREENS.TEACHER_DETAIL)) {
                    showDetail(forward);
                    return;
                }
            }
            super.applyCommand(command);
        }

        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            switch (screenKey) {
                case SCREENS.GROUPS_LIST:
                    return new GroupsFragment();
                case SCREENS.TEACHERS_LIST:
                    return new TeachersFragment();
                case SCREENS.MY_SCHEDULE:
                    return new MyScheduleFragment();
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

        private void showDetail(Forward forward) {
            NamedEntity data = (NamedEntity) forward.getTransitionData();
            Class activity = DetailGroupActivity.class;
            if (forward.getScreenKey().equals(SCREENS.GROUP_DETAIL)) {
                activity = DetailGroupActivity.class;
            }
            if (forward.getScreenKey().equals(SCREENS.TEACHER_DETAIL)) {
                activity = DetailTeacherActivity.class;
            }
            Intent intent = new Intent(MainActivity.this, activity);
            intent.putExtra(Intent.EXTRA_TEXT, data.getId());
            intent.putExtra(Intent.EXTRA_TITLE, data.getName());
            startActivity(intent);
        }
    }
}
