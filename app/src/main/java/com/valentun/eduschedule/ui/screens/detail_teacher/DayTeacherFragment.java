package com.valentun.eduschedule.ui.screens.detail_teacher;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.valentun.eduschedule.ui.common.fragments.RecyclerViewFragment;
import com.valentun.eduschedule.ui.common.views.ListView;
import com.valentun.parser.pojo.Lesson;

import java.util.List;

public class DayTeacherFragment extends RecyclerViewFragment<Lesson>
        implements ListView<Lesson> {

    private static final String DAY_NUMBER_KEY = "DAY_NUMBER";
    private static final String TEACHER_ID_KEY = "TEACHER_ID";
    @InjectPresenter
    DayTeacherPresenter presenter;

    public static DayTeacherFragment newInstance(String teacherId, int dayNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(DAY_NUMBER_KEY, dayNumber);
        bundle.putString(TEACHER_ID_KEY, teacherId);

        DayTeacherFragment fragment = new DayTeacherFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @ProvidePresenter
    public DayTeacherPresenter providePresenter() {
        return new DayTeacherPresenter(getTeacherId(), getDayNumber());
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Lesson> data) {
        return new DayTeacherAdapter(data);
    }

    private int getDayNumber() {
        return getArguments().getInt(DAY_NUMBER_KEY);
    }

    private String getTeacherId() {
        return getArguments().getString(TEACHER_ID_KEY);
    }
}
