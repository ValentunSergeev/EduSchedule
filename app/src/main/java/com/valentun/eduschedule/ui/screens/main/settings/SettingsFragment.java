package com.valentun.eduschedule.ui.screens.main.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.jobs.JobManager;

import javax.inject.Inject;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Inject
    JobManager jobManager;

    @Inject
    IRepository repository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDagger();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);

        PreferenceScreen screen = getPreferenceScreen();

        screen.findPreference(Constants.SETTINGS.SETTINGS_NOTIFICATIONS)
                .setOnPreferenceChangeListener((preference, newValue) -> {
                    Boolean isEnabled = (Boolean) newValue;
                    if (isEnabled) {
                        jobManager.startJob(Constants.JOBS.CHECK_SCHEDULE);
                    } else {
                        jobManager.stopJob(Constants.JOBS.CHECK_SCHEDULE);
                    }

                    return true;
                });

        screen.findPreference(Constants.SETTINGS.SETTINGS_SCHEDULE_TYPE)
                .setOnPreferenceChangeListener(((preference, newValue) -> {
                    repository.clearMyScheduleObjectId();
                    return true;
                }));
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getActivity() != null)
            getActivity().setTitle(R.string.settings);
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }
}
