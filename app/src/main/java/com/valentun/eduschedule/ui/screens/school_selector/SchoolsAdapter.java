package com.valentun.eduschedule.ui.screens.school_selector;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.valentun.eduschedule.data.dto.SchoolInfo;
import com.valentun.eduschedule.databinding.ItemSchoolBinding;

import java.util.List;

public class SchoolsAdapter extends RecyclerView.Adapter<SchoolsAdapter.SchoolsHolder> {
    private List<SchoolInfo> schools;
    private EventHandler handler;

    SchoolsAdapter(List<SchoolInfo> schools, EventHandler handler) {
        this.schools = schools;
        this.handler = handler;
    }

    @Override
    public SchoolsAdapter.SchoolsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemSchoolBinding itemBinding = ItemSchoolBinding.inflate(layoutInflater, parent, false);
        return new SchoolsAdapter.SchoolsHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(SchoolsAdapter.SchoolsHolder holder, int position) {
        SchoolInfo schoolInfo = schools.get(position);

        holder.bind(schoolInfo);
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    public interface EventHandler {
        void itemClicked(SchoolInfo item);
    }

    class SchoolsHolder extends RecyclerView.ViewHolder {
        private ItemSchoolBinding binding;

        SchoolsHolder(ItemSchoolBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(SchoolInfo schoolInfo) {
            binding.setSchool(schoolInfo);
            binding.setHandler(handler);
        }
    }
}
