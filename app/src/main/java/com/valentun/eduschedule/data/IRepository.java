package com.valentun.eduschedule.data;


import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.Lesson;
import com.valentun.parser.pojo.School;

import java.util.List;

import io.reactivex.Observable;

public interface IRepository {
    Observable<School> getSchool();

    Observable<List<Lesson>> getGroupSchedule(String groupId, int dayNumber);

    Observable<List<Lesson>> getTeacherSchedule(String teacherId, int dayNumber);

    Observable<List<Group>> getGroups();

    boolean isGroupChosen();
    String getGroupId();
    void setGroupId(String groupId);
    void clearGroupId();
}
