package com.valentun.eduschedule.ui.screens.detail_teacher;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.valentun.eduschedule.R;

import static android.support.v7.appcompat.R.style;

/**
 * Created by Sergey on 25.10.2017.
 */

public class DayTeacherBindingAdapter {
    @BindingAdapter("lesson_name_teacher")
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

    @BindingAdapter("room_number")
    public static void bindRoom(TextView view, String room) {
        if (TextUtils.isEmpty(room)) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(room);
            view.setVisibility(View.VISIBLE);
        }
    }
}
