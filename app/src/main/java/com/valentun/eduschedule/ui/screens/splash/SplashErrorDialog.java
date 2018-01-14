package com.valentun.eduschedule.ui.screens.splash;

import android.support.annotation.StringRes;

import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.ui.common.dialogs.SimpleDialog;

class SplashErrorDialog extends SimpleDialog {
    SplashErrorDialog(@StringRes int error, boolean displayUseCache) {
        super();

        negativeText = MyApplication.INSTANCE.getString(R.string.exit);
        positiveText = MyApplication.INSTANCE.getString(R.string.retry);

        if(displayUseCache)
            neutralText = MyApplication.INSTANCE.getString(R.string.use_cache);

        title = MyApplication.INSTANCE.getString(error);
    }
}
