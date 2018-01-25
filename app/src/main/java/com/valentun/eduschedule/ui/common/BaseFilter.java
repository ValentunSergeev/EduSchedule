package com.valentun.eduschedule.ui.common;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFilter<T> extends Filter {
    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults filterResults = new FilterResults();
        if (charSequence != null) {
            List<T> results = findResult(charSequence);
            filterResults.values = results;
            filterResults.count = results.size();
        }
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        List<T> result;

        if (filterResults != null && filterResults.values != null) {
            result = (List<T>) filterResults.values;
        } else {
            result = new ArrayList<>();
        }

        showResult(result);
    }

    protected abstract void showResult(List<T> result);

    protected abstract List<T> findResult(CharSequence query);
}