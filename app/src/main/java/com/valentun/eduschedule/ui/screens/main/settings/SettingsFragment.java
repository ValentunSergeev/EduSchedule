package com.valentun.eduschedule.ui.screens.main.settings;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.valentun.eduschedule.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getActivity() != null)
            getActivity().setTitle(R.string.settings);
    }
}
