package com.valentun.eduschedule.ui.screens.detail_group;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.valentun.eduschedule.BR;
import com.valentun.eduschedule.R;
import com.valentun.parser.pojo.Lesson;

import java.util.List;

class DayGroupAdapter extends RecyclerView.Adapter<DayGroupAdapter.LessonHolder> {
    private static final int SINGLE_LESSON = R.layout.item_group_lesson;
    private static final int DOUBLE_LESSON = R.layout.item_double_lesson;

    private List<Lesson> lessons;

    DayGroupAdapter(List<Lesson> lessons) {
        this.lessons = lessons;
    }


    @Override
    public DayGroupAdapter.LessonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        return new DayGroupAdapter.LessonHolder(binding);
    }

    @Override
    public void onBindViewHolder(DayGroupAdapter.LessonHolder holder, int position) {
        Lesson lesson = lessons.get(position);

        holder.bind(lesson);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    @Override
    public int getItemViewType(int position) {
        return lessons.get(position).isSingle() ? SINGLE_LESSON : DOUBLE_LESSON;
    }

    class LessonHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        LessonHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Lesson lesson) {
            binding.setVariable(BR.lesson, lesson);
        }
    }
}

