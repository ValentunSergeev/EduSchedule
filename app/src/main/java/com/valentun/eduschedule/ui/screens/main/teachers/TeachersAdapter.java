package com.valentun.eduschedule.ui.screens.main.teachers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.valentun.eduschedule.databinding.ItemTeacherBinding;
import com.valentun.parser.pojo.NamedEntity;
import com.valentun.parser.pojo.Teacher;

import java.util.List;

/**
 * Created by Sergey on 24.10.2017.
 */

public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.TeachersHolder> {
    private List<Teacher> teachers;
    private EventHandler handler;

    TeachersAdapter(List<Teacher> teachers, EventHandler handler) {
        this.teachers = teachers;
        this.handler = handler;
    }

    @Override
    public TeachersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTeacherBinding itemBinding = ItemTeacherBinding.inflate(layoutInflater, parent, false);
        return new TeachersHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(TeachersHolder holder, int position) {
        Teacher teacher = teachers.get(position);

        holder.bind(teacher);
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public interface EventHandler {
        void itemClicked(NamedEntity item);
    }

    class TeachersHolder extends RecyclerView.ViewHolder {
        private ItemTeacherBinding binding;

        TeachersHolder(ItemTeacherBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Teacher teacher) {
            binding.setTeacher(teacher);
            binding.setHandler(handler);
        }
    }
}
