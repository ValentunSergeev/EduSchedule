package com.valentun.eduschedule.ui.common.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ScreenListBinding;
import com.valentun.eduschedule.ui.common.views.ListView;

import java.util.List;

public abstract class RecyclerViewFragment<T> extends MvpAppCompatFragment
        implements ListView<T> {
    protected ScreenListBinding binding;

    private SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        private Handler handler = new Handler();
        private Filterable filterable = getFilterable();

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            handler.removeCallbacksAndMessages(null);

            binding.progress.setVisibility(View.VISIBLE);
            binding.list.setVisibility(View.GONE);

            handler.postDelayed(() -> filterable.getFilter().filter(newText, i -> {
                binding.list.setVisibility(View.VISIBLE);
            }), Constants.SEARCH_DELAY);
            return true;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.screen_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        binding.list.setHasFixedSize(true);
        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void showData(List<T> data) {
        hideProgress();
        binding.list.setAdapter(getAdapter(data));
    }

    private void hideProgress() {
        binding.progress.setVisibility(View.GONE);
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
        hideProgress();

        binding.placeholder.setVisibility(View.VISIBLE);
        binding.placeholder.setText(getPlaceholderText());
    }

    @Override
    public void showError(@StringRes int errorRes) {
        hideProgress();

        Snackbar.make(binding.getRoot(), errorRes, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, v -> {
                    showProgress();
                    retryClicked();
                })
                .show();
    }

    /**
     * Override to add {@link SearchView} to toolbar
     **/
    protected Filterable getFilterable() {
        return null;
    }

    /**
     * Override to change {@link SearchView} input type.
     * Won't work if {@link RecyclerViewFragment#getFilterable()} isn't overridden
     **/
    protected int getFilterType() {
        return InputType.TYPE_CLASS_TEXT;
    }

    protected abstract RecyclerView.Adapter getAdapter(List<T> data);

    protected abstract String getPlaceholderText();

    protected abstract void retryClicked();

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getFilterable() != null) {
            inflater.inflate(R.menu.filterable_menu, menu);

            final MenuItem searchItem = menu.findItem(R.id.action_search);
            final SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setInputType(getFilterType());
            searchView.setOnQueryTextListener(listener);
        }
    }


}
