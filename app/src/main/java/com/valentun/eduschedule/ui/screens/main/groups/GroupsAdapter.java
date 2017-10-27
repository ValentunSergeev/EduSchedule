package com.valentun.eduschedule.ui.screens.main.groups;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.valentun.eduschedule.databinding.ItemGroupBinding;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.NamedEntity;

import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsHolder> {
    private List<Group> groups;
    private EventHandler handler;

    public interface EventHandler {
        void itemClicked(NamedEntity item);
    }

    public GroupsAdapter(List<Group> groups, EventHandler handler) {
        this.groups = groups;
        this.handler = handler;
    }

    @Override
    public GroupsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ItemGroupBinding itemBinding =
                ItemGroupBinding.inflate(layoutInflater, parent, false);
        return new GroupsHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(GroupsHolder holder, int position) {
        Group group = groups.get(position);

        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class GroupsHolder extends RecyclerView.ViewHolder {
        private ItemGroupBinding binding;

        GroupsHolder(ItemGroupBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Group group) {
            binding.setGroup(group);
            binding.setHandler(handler);
        }
    }
}
