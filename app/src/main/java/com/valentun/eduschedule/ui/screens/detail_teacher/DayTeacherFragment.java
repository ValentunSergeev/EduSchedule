package com.valentun.eduschedule.ui.screens.detail_teacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.valentun.eduschedule.R;
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

    private boolean isVisible;

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

    @Override
    protected String getPlaceholderText() {
        return getString(R.string.no_lessons);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(isVisible && !isHasAdapter()) {
            presenter.getData();
        }
    }

    @Override
    public void showData(List<Lesson> data) {
        if (isVisible) super.showData(data);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        isVisible = isVisibleToUser;

        if (isVisibleToUser && presenter != null && !isHasAdapter()) {
            presenter.getData();
        }
    }

    private int getDayNumber() {
        return getArguments().getInt(DAY_NUMBER_KEY);
    }

    private String getTeacherId() {
        return getArguments().getString(TEACHER_ID_KEY);
    }
}
