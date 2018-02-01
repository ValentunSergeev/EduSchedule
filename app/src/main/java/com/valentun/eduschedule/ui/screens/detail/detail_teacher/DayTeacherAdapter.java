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
import com.valentun.eduschedule.ui.common.dialogs.OptionsDialog;
import com.valentun.eduschedule.utils.DateUtils;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.Lesson;
import com.valentun.parser.pojo.Period;

import java.util.List;

public class DayTeacherAdapter extends RecyclerView.Adapter<DayTeacherAdapter.LessonHolder> {
    private static final int SINGLE_LESSON = R.layout.item_teacher_lesson;

    private List<Lesson> lessons;
    private boolean isCurrentDay;
    private Handler handler;

    DayTeacherAdapter(List<Lesson> lessons, boolean isCurrentDay, Handler handler) {
        this.lessons = lessons;
        this.isCurrentDay = isCurrentDay;
        this.handler = handler;
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

    interface Handler {
        void showGroup(Group group);
    }

    class LessonHolder extends RecyclerView.ViewHolder {
        private ItemTeacherLessonBinding binding;
        private View indicator;

        LessonHolder(ItemTeacherLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            indicator = binding.indicator;
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

            Group group = current.getGroup();
            binding.getRoot().setOnClickListener(v ->
                    new OptionsDialog(group.getName())
                            .show(indicator.getContext())
                            .subscribe(ignored -> handler.showGroup(group))
            );
        }
    }
}
