package com.valentun.eduschedule.ui.screens.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.Constants.SCREENS;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ActivityMainBinding;
import com.valentun.eduschedule.ui.common.BaseActivity;
import com.valentun.eduschedule.ui.common.callbacks.BackButtonListener;
import com.valentun.eduschedule.ui.screens.detail.DetailActivity;
import com.valentun.eduschedule.ui.screens.main.choose_group.ChooseGroupFragment;
import com.valentun.eduschedule.ui.screens.main.choose_teacher.ChooseTeacherFragment;
import com.valentun.eduschedule.ui.screens.main.choose_type.ChooseTypeFragment;
import com.valentun.eduschedule.ui.screens.main.groups.GroupsFragment;
import com.valentun.eduschedule.ui.screens.main.info.InfoFragment;
import com.valentun.eduschedule.ui.screens.main.my_schedule.MyScheduleFragment;
import com.valentun.eduschedule.ui.screens.main.settings.SettingsFragment;
import com.valentun.eduschedule.ui.screens.main.teachers.TeachersFragment;
import com.valentun.eduschedule.ui.screens.school_selector.SchoolSelectActivity;
import com.valentun.eduschedule.ui.screens.splash.SplashActivity;
import com.valentun.parser.pojo.NamedEntity;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;
import ru.terrakok.cicerone.commands.Back;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Forward;
import ru.terrakok.cicerone.commands.Replace;
import ru.terrakok.cicerone.commands.SystemMessage;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, IBarView, MainView {

    public static final String SCREEN_FRAGMENT_KEY = "SCREEN_FRAGMENT_KEY";

    @Inject
    NavigatorHolder navigatorHolder;
    @InjectPresenter
    MainPresenter presenter;

    private String fragmentScreen;

    private Navigator navigator;
    private ActivityMainBinding binding;
    private ActionBar actionBar;
    private ActionBarDrawerToggle drawerToggle;

    private boolean isBackMode = false;
    private Handler backModeHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.INSTANCE.getAppComponent().inject(this);

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initToolBar();

        navigator = new MainNavigator(getSupportFragmentManager(), R.id.main_container);

        if (savedInstanceState == null) {
            fragmentScreen = SCREENS.MY_SCHEDULE;
            if (getIntent().hasExtra(SCREEN_FRAGMENT_KEY)) {
                fragmentScreen = getIntent().getStringExtra(SCREEN_FRAGMENT_KEY);
            }
            switch (fragmentScreen) {
                case SCREENS.GROUPS_LIST:
                    binding.navView.setCheckedItem(R.id.nav_item_groups);
                    break;
                case SCREENS.TEACHERS_LIST:
                    binding.navView.setCheckedItem(R.id.nav_item_teachers);
                    break;
                default:
                    binding.navView.setCheckedItem(R.id.nav_item_my_schedule);
                    break;
            }
            presenter.loadName();
            navigator.applyCommand(new Replace(fragmentScreen, null));
        }
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
            fragmentScreen = getFragmentScreen();
            presenter.forceRefreshClicked();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private String getFragmentScreen() {
        Fragment fragment = getSupportFragmentManager().getFragments().get(0);
        if (fragment instanceof TeachersFragment) {
            return SCREENS.TEACHERS_LIST;
        } else if (fragment instanceof GroupsFragment) {
            return SCREENS.GROUPS_LIST;
        } else if (fragment instanceof ChooseGroupFragment) {
            return SCREENS.CHOOSE_GROUP;
        } else {
            return SCREENS.MY_SCHEDULE;
        }
    }

    @Override
    public void showName(String name) {
        TextView school_name = binding.navView.getHeaderView(0).findViewById(R.id.school_name);
        school_name.setText(name);
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
        String screenKey;
        switch (item.getItemId()) {
            case R.id.nav_item_groups:
                screenKey = SCREENS.GROUPS_LIST;
                break;
            case R.id.nav_item_teachers:
                screenKey = SCREENS.TEACHERS_LIST;
                break;
            case R.id.nav_item_my_schedule:
                screenKey = SCREENS.MY_SCHEDULE;
                break;
            case R.id.nav_item_change_school:
                screenKey = SCREENS.SCHOOL_SELECTOR;
                break;
            case R.id.nav_item_settings:
                screenKey = SCREENS.SETTINGS;
                break;
            case R.id.nav_logo_info:
                screenKey = SCREENS.INFO;
                break;
            default:
                screenKey = SCREENS.MY_SCHEDULE;
        }

        Command command;

        if (screenKey.equals(SCREENS.INFO)) {
            command = new Forward(screenKey, null);
        } else {
            command = new Replace(screenKey, null);
        }
        navigator.applyCommand(command);
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
        }

        if (isBackMode) {
            finish();
        } else {
            isBackMode = true;
            navigator.applyCommand(new SystemMessage(getString(R.string.back_mode_message)));
            backModeHandler.removeCallbacksAndMessages(null);
            backModeHandler.postDelayed(() -> isBackMode = false, Constants.BACK_MODE_TIME);
        }
    }

    @Override
    public AppBarLayout getToolbarContainer() {
        return binding.appbarLayout;
    }

    @Override
    public void setUpArrowEnabled(boolean enabled) {
        drawerToggle.setDrawerIndicatorEnabled(!enabled);
        actionBar.setDisplayHomeAsUpEnabled(enabled);

        if (enabled) {
            drawerToggle.setToolbarNavigationClickListener(v -> navigator.applyCommand(new Back()));
        } else {
            drawerToggle.setToolbarNavigationClickListener(null);
        }

        drawerToggle.syncState();
    }

    private class MainNavigator extends SupportFragmentNavigator {
        MainNavigator(FragmentManager fragmentManager, int containerId) {
            super(fragmentManager, containerId);
        }

        @Override
        public void applyCommand(Command command) {
            if (command instanceof Replace) {
                Replace replace = (Replace) command;
                if (replace.getScreenKey().equals(SCREENS.SCHOOL_SELECTOR)) {
                    presenter.schoolChangeClicked();
                    showSchoolSelector();
                    return;
                } else if (replace.getScreenKey().equals(SCREENS.FORCE_REFRESH)) {
                    Intent intent = buildNewTaskIntent(SplashActivity.class);
                    intent.putExtra(SplashActivity.EXTRA_FORCE_UPDATE, true);
                    intent.putExtra(SplashActivity.SCREEN_RETURN_KEY, fragmentScreen);
                    startActivity(intent);
                    return;
                }
            }

            if (command instanceof Forward) {
                Forward forward = (Forward) command;
                if (forward.getScreenKey().equals(SCREENS.GROUP_DETAIL) ||
                        forward.getScreenKey().equals(SCREENS.TEACHER_DETAIL)) {
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
                case SCREENS.CHOOSE_GROUP:
                    return new ChooseGroupFragment();
                case SCREENS.SETTINGS:
                    return new SettingsFragment();
                case SCREENS.SCHEDULE_TYPE_CHOOSER:
                    return new ChooseTypeFragment();
                case SCREENS.CHOOSE_TEACHER:
                    return new ChooseTeacherFragment();
                case SCREENS.INFO:
                    return new InfoFragment();
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

        private void showSchoolSelector() {
            Intent intent = new Intent(MainActivity.this, SchoolSelectActivity.class);
            startActivity(intent);

            exit();
        }

        private void showDetail(Forward forward) {
            NamedEntity data = (NamedEntity) forward.getTransitionData();

            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_TYPE, forward.getScreenKey());
            intent.putExtra(Intent.EXTRA_TEXT, data.getId());
            intent.putExtra(Intent.EXTRA_TITLE, data.getName());

            startActivity(intent);
        }
    }

}
