package com.valentun.eduschedule.ui.screens.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.ui.common.BaseActivity;
import com.valentun.eduschedule.ui.screens.detail.DetailActivity;
import com.valentun.eduschedule.ui.screens.main.MainActivity;
import com.valentun.eduschedule.ui.screens.school_selector.SchoolSelectActivity;
import com.valentun.parser.pojo.NamedEntity;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.SupportAppNavigator;


public class SplashActivity extends BaseActivity implements SplashView {
    public static final String EXTRA_FORCE_UPDATE = "FORCE_UPDATE";
    public static final String SCREEN_RETURN_KEY = "SCREEN_RETURN_KEY";
    public static final String SCREEN_DETAIL_RETURN_KEY = "SCREEN_DETAIL_RETURN_KEY";
    @Inject
    NavigatorHolder navigatorHolder;
    @InjectPresenter
    SplashPresenter presenter;
    private Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        navigator = new SplashNavigator(this);

        boolean forceUpdate = getIntent().getBooleanExtra(EXTRA_FORCE_UPDATE, false);

        String screenKey = Constants.SCREENS.MAIN;
        if (getIntent().hasExtra(SCREEN_RETURN_KEY)) {
            screenKey = getIntent().getStringExtra(SCREEN_RETURN_KEY);
        }

        NamedEntity data = null;
        if (getIntent().hasExtra(SCREEN_DETAIL_RETURN_KEY)) {
            ArrayList<String> transition = getIntent().getStringArrayListExtra(SCREEN_DETAIL_RETURN_KEY);
            screenKey = transition.get(0);
            data = new NamedEntity(transition.get(1), transition.get(2));
        }
        presenter.loadSchool(forceUpdate, screenKey, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    public void showError(@StringRes int stringRes, boolean displayUseCache) {
        new SplashErrorDialog(stringRes, displayUseCache).show(this)
                .subscribe(buttonId -> {
                    switch (buttonId) {
                        case SplashErrorDialog.POSITIVE_CLICK:
                            presenter.retry();
                            break;
                        case SplashErrorDialog.NEGATIVE_CLICK:
                            presenter.exit();
                            break;
                        case SplashErrorDialog.NEUTRAL_CLICK:
                            presenter.useCache();
                    }
                });
    }

    private class SplashNavigator extends SupportAppNavigator {
        private SplashNavigator(FragmentActivity activity) {
            super(activity, 0);
        }

        @Override
        protected Intent createActivityIntent(String screenKey, Object data) {
            NamedEntity transitionData = (NamedEntity) data;
            Intent intent;
            switch (screenKey) {
                case Constants.SCREENS.SCHOOL_SELECTOR:
                    intent = buildNewTaskIntent(SchoolSelectActivity.class);
                    break;
                case Constants.SCREENS.GROUP_DETAIL:
                case Constants.SCREENS.TEACHER_DETAIL:
                    intent = buildNewTaskIntent(DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_TYPE, screenKey);
                    intent.putExtra(Intent.EXTRA_TEXT, transitionData.getId());
                    intent.putExtra(Intent.EXTRA_TITLE, transitionData.getName());
                    break;
                case Constants.SCREENS.MAIN:
                    intent = buildNewTaskIntent(MainActivity.class);
                    break;
                case Constants.SCREENS.TEACHERS_LIST:
                case Constants.SCREENS.GROUPS_LIST:
                case Constants.SCREENS.MY_SCHEDULE:
                case Constants.SCREENS.CHOOSE_GROUP:
                    intent = buildNewTaskIntent(MainActivity.class);
                    intent.putExtra(MainActivity.SCREEN_FRAGMENT_KEY, screenKey);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown screen");
            }

            return intent;
        }

        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            return null;
        }

        @Override
        protected void showSystemMessage(String message) {
            Toast.makeText(SplashActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }
}
