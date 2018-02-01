package com.valentun.eduschedule.ui.screens.detail.detail_teacher;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valentun.eduschedule.BR;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ItemTeacherLessonBinding;
import com.valentun.eduschedule.utils.DateUtils;
import com.valentun.parser.pojo.Lesson;
import com.valentun.parser.pojo.Period;

import java.util.List;

public class DayTeacherAdapter extends RecyclerView.Adapter<DayTeacherAdapter.LessonHolder> {
    private static final int SINGLE_LESSON = R.layout.item_teacher_lesson;

    private List<Lesson> lessons;
    private boolean isCurrentDay;

    DayTeacherAdapter(List<Lesson> lessons, boolean isCurrentDay) {
        this.lessons = lessons;
        this.isCurrentDay = isCurrentDay;
    }

    @Override
    public DayTeacherAdapter.LessonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ItemTeacherLessonBinding binding = DataBindingUtil.inflate(layoutInflater, SINGLE_LESSON, parent, false);
        return new DayTeacherAdapter.LessonHolder(binding);
    }

    @Override
    public void onBindViewHolder(DayTeacherAdapter.LessonHolder holder, int position) {
        Lesson current = lessons.get(position);
        Lesson previous = position > 0 ? lessons.get(position - 1) : null;

        holder.bind(current, previous);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }


    class LessonHolder extends RecyclerView.ViewHolder {
        private ItemTeacherLessonBinding binding;

        LessonHolder(ItemTeacherLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressWarnings("RedundantCast")
        void bind(Lesson current, @Nullable Lesson previous) {
            binding.setVariable(BR.lesson, current);

            Period previousPeriod = previous != null ? previous.getPeriod() : null;

            int visibility;

            if (isCurrentDay && DateUtils.isPeriodCurrentOrNext(current.getPeriod(), previousPeriod)) {
                visibility = View.VISIBLE;
            } else {
                visibility = View.GONE;
            }

            binding.indicator.setVisibility(visibility);
        }
    }
}
