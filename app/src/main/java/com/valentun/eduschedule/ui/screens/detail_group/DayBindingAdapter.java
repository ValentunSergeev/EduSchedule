package com.valentun.eduschedule.ui.screens.detail_group;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.valentun.eduschedule.R;

import static android.support.v7.appcompat.R.style;

public class DayBindingAdapter {

    @BindingAdapter("lesson_name")
    public static void bindLessonName(TextView view, String subjectName) {
        int appearance = -1;

        if (TextUtils.isEmpty(subjectName)) {
            subjectName = view.getContext().getString(R.string.no_lesson);
            appearance = style.TextAppearance_AppCompat_Medium;
        } else {
            appearance = style.TextAppearance_AppCompat_Title;
        }

        view.setText(subjectName);
        view.setTextAppearance(view.getContext(), appearance);
    }

    @BindingAdapter("teacher_name")
    public static void bindTeacherName(TextView view, String name) {
        if (TextUtils.isEmpty(name)) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(name);
            view.setVisibility(View.VISIBLE);
        }
    }
}
