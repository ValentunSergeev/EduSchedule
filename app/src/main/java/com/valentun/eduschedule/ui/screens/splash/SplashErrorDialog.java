package com.valentun.eduschedule.ui.screens.splash;

import android.support.annotation.StringRes;

import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.ui.common.dialogs.SimpleDialog;

public class SplashErrorDialog extends SimpleDialog {
    public SplashErrorDialog(@StringRes int error) {
        super();

        negativeText = MyApplication.INSTANCE.getString(R.string.exit);
        positiveText = MyApplication.INSTANCE.getString(R.string.retry);

        title = MyApplication.INSTANCE.getString(error);
    }
}
