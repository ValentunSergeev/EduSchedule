package com.valentun.eduschedule.ui.common;

import android.app.Activity;
import android.content.Intent;

import com.arellomobile.mvp.MvpAppCompatActivity;

public abstract class BaseActivity extends MvpAppCompatActivity {
    protected Intent buildNewTaskIntent (Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
