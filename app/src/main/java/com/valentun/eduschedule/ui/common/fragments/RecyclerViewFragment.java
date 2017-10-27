package com.valentun.eduschedule.ui.common.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ScreenListBinding;
import com.valentun.eduschedule.ui.common.views.ListView;

import java.util.List;

public abstract class RecyclerViewFragment<T> extends MvpAppCompatFragment implements ListView<T> {
    protected ScreenListBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.screen_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        binding.list.setHasFixedSize(true);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void showData(List<T> data) {
        binding.progress.setVisibility(View.GONE);
        binding.list.setAdapter(getAdapter(data));
    }

    @Override
    public void showProgress() {
        binding.list.setAdapter(null);
        binding.progress.setVisibility(View.VISIBLE);
    }

    protected boolean isHasAdapter() {
        return binding.list.getAdapter() != null;
    }

    @Override
    public void showPlaceholder() {
        binding.progress.setVisibility(View.GONE);

        binding.placeholder.setVisibility(View.VISIBLE);
        binding.placeholder.setText(getPlaceholderText());
    }

    protected abstract RecyclerView.Adapter getAdapter(List<T> data);

    protected abstract String getPlaceholderText();
}
