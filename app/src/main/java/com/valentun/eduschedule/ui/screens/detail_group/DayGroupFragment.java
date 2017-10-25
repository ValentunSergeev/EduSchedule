package com.valentun.eduschedule.ui.screens.detail_group;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.valentun.eduschedule.ui.common.fragments.RecyclerViewFragment;
import com.valentun.eduschedule.ui.common.views.ListView;
import com.valentun.parser.pojo.Lesson;

import java.util.List;

public class DayGroupFragment extends RecyclerViewFragment<Lesson>
        implements ListView<Lesson> {

    private static final String DAY_NUMBER_KEY = "DAY_NUMBER";
    private static final String GROUP_ID_KEY = "GROUP_ID";
    @InjectPresenter
    DayGroupPresenter presenter;

    public static DayGroupFragment newInstance(String groupId, int dayNumber) {
        Bundle bundle = new Bundle();
        bundle.putInt(DAY_NUMBER_KEY, dayNumber);
        bundle.putString(GROUP_ID_KEY, groupId);

        DayGroupFragment fragment = new DayGroupFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @ProvidePresenter
    public DayGroupPresenter providePresenter() {
        return new DayGroupPresenter(getGroupId(), getDayNumber());
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<Lesson> data) {
        return new DayGroupAdapter(data);
    }

    private int getDayNumber() {
        return getArguments().getInt(DAY_NUMBER_KEY);
    }

    private String getGroupId() {
        return getArguments().getString(GROUP_ID_KEY);
    }
}