package com.valentun.eduschedule.ui.screens.school_selector;

import android.support.v7.widget.RecyclerView;
import android.widget.Filterable;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.data.dto.SchoolInfo;
import com.valentun.eduschedule.ui.common.fragments.RecyclerViewFragment;
import com.valentun.eduschedule.ui.common.views.ListView;

import java.util.List;


public class SchoolSelectFragment extends RecyclerViewFragment<SchoolInfo>
        implements ListView<SchoolInfo>,SchoolsAdapter.EventHandler {

    @InjectPresenter
    SchoolSelectPresenter presenter;

    @Override
    protected Filterable getFilterable() {
        return () -> presenter.filter;
    }

    @Override
    protected RecyclerView.Adapter getAdapter(List<SchoolInfo> data) {
        return new SchoolsAdapter(data, this);
    }

    @Override
    protected String getPlaceholderText() {
        return getString(R.string.no_schools);
    }

    @Override
    protected void retryClicked() {
        presenter.getData();
    }


    @Override
    public void itemClicked(SchoolInfo item) {
        presenter.schoolSelected(item.getId());
    }
}
