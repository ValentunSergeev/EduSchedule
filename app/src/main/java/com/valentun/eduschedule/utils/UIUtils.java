package com.valentun.eduschedule.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

@SuppressWarnings({"unused", "WeakerAccess", "ConstantConditions"})
public class UIUtils {
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        hideKeyboard(view);
    }

    public static void hideKeyboard(View view) {
        if (view != null) {
            Context context = view.getContext();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
