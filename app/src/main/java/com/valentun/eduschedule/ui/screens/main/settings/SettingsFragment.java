package com.valentun.eduschedule.ui.screens.main.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.jobs.JobManager;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);

        getPreferenceScreen().findPreference(Constants.SETTINGS.SETTINGS_NOTIFICATIONS)
                .setOnPreferenceChangeListener((preference, newValue) -> {
                    Boolean isEnabled = (Boolean) newValue;
                    JobManager manager = new JobManager(getContext());

                    if (isEnabled) {
                        manager.startJob(Constants.JOBS.CHECK_SCHEDULE);
                    } else {
                        manager.stopJob(Constants.JOBS.CHECK_SCHEDULE);
                    }

                    return true;
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getActivity() != null)
            getActivity().setTitle(R.string.settings);
    }
}
