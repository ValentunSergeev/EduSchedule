package com.valentun.eduschedule.ui.screens.detail_teacher;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.valentun.eduschedule.BR;
import com.valentun.eduschedule.R;
import com.valentun.parser.pojo.Lesson;

import java.util.List;

public class DayTeacherAdapter extends RecyclerView.Adapter<DayTeacherAdapter.LessonHolder> {
    private static final int SINGLE_LESSON = R.layout.item_teacher_lesson;
    private List<Lesson> lessons;

    DayTeacherAdapter(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    @Override
    public DayTeacherAdapter.LessonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, SINGLE_LESSON, parent, false);
        return new DayTeacherAdapter.LessonHolder(binding);
    }

    @Override
    public void onBindViewHolder(DayTeacherAdapter.LessonHolder holder, int position) {
        Lesson lesson = lessons.get(position);

        holder.bind(lesson);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
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
