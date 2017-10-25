package com.valentun.eduschedule.data;


import com.valentun.parser.pojo.Lesson;
import com.valentun.parser.pojo.School;

import java.util.List;

import io.reactivex.Observable;

public interface IRepository {
    Observable<School> getSchool();

    Observable<List<Lesson>> getGroupSchedule(String groupId, int dayNumber);

    Observable<List<Lesson>> getTeacherSchedule(String teacherId, int dayNumber);
}
