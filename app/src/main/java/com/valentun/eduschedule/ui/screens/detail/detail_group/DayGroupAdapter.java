package com.valentun.eduschedule.ui.screens.detail.detail_group;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valentun.eduschedule.BR;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.databinding.ItemDoubleLessonBinding;
import com.valentun.eduschedule.databinding.ItemGroupLessonBinding;
import com.valentun.eduschedule.ui.common.dialogs.OptionsDialog;
import com.valentun.eduschedule.utils.DateUtils;
import com.valentun.parser.pojo.Lesson;
import com.valentun.parser.pojo.Period;
import com.valentun.parser.pojo.SingleLesson;
import com.valentun.parser.pojo.SubGroupLesson;
import com.valentun.parser.pojo.Teacher;

import java.util.List;

class DayGroupAdapter extends RecyclerView.Adapter<DayGroupAdapter.LessonHolder> {
    private static final int SINGLE_LESSON = R.layout.item_group_lesson;
    private static final int DOUBLE_LESSON = R.layout.item_double_lesson;

    private List<Lesson> lessons;
    private boolean isCurrentDay;
    private Handler handler;

    DayGroupAdapter(List<Lesson> lessons, boolean isCurrentDay, Handler handler) {
        this.lessons = lessons;
        this.isCurrentDay = isCurrentDay;
        this.handler = handler;
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
        Lesson current = lessons.get(position);
        Lesson previous = position > 0 ? lessons.get(position - 1) : null;

        holder.bind(current, previous);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    @Override
    public int getItemViewType(int position) {
        return lessons.get(position).isSingle() ? SINGLE_LESSON : DOUBLE_LESSON;
    }

    interface Handler {
        void showTeacher(Teacher teacher);
    }

    class LessonHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;
        private View indicator;

        LessonHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            if (binding instanceof ItemGroupLessonBinding) {
                indicator = ((ItemGroupLessonBinding) binding).indicator;
            } else {
                indicator = ((ItemDoubleLessonBinding) binding).indicator;
            }
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

            indicator.setVisibility(visibility);

            if (getItemViewType() == SINGLE_LESSON) {
                bindSingle((SingleLesson) current);
            } else {
                bindDouble((SubGroupLesson) current);
            }
        }

        private void bindDouble(SubGroupLesson lesson) {
            List<SingleLesson> subLessons = lesson.getSubLessons();
            Teacher teacher1 = subLessons.get(0).getTeacher();
            Teacher teacher2 = subLessons.get(1).getTeacher();

            binding.getRoot().setOnClickListener(v ->
                    new OptionsDialog(teacher1.getName(), teacher2.getName())
                            .show(indicator.getContext())
                            .subscribe(which ->
                                    handler.showTeacher(subLessons.get(which).getTeacher()))
            );
        }

        private void bindSingle(SingleLesson lesson) {
            Teacher teacher = lesson.getTeacher();
            binding.getRoot().setOnClickListener(v ->
                    new OptionsDialog(teacher.getName())
                            .show(indicator.getContext())
                            .subscribe(ignored -> handler.showTeacher(teacher))
            );
        }
    }
}

