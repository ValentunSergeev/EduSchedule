package com.valentun.eduschedule.data.network;

import android.support.annotation.StringRes;

import com.valentun.eduschedule.R;

public class ErrorHandler {
    private static final String NO_INTERNET_PREFIX = "Unable to resolve";
    private static final String TIMEOUT_PREFIX = "Timeout";

    @StringRes
    public static int getErrorMessage(Throwable error) {
        String cause = error.getMessage();

        if (cause.startsWith(NO_INTERNET_PREFIX)) {
            return R.string.no_internet;
        } else if (cause.startsWith(TIMEOUT_PREFIX)) {
            return R.string.timeout;
        } else {
            return R.string.unknown_error;
        }
    }
}
