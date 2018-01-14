package com.valentun.eduschedule.data;


import com.valentun.eduschedule.data.dto.SchoolInfo;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.Lesson;
import com.valentun.parser.pojo.School;
import com.valentun.parser.pojo.Teacher;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface IRepository {
    Observable<List<Lesson>> getGroupSchedule(String groupId, int dayNumber);

    Observable<List<Lesson>> getTeacherSchedule(String teacherId, int dayNumber);

    Observable<List<Group>> getGroups();

    Observable<List<Teacher>> getTeachers();

    Observable<String> getSchoolName();

    Observable<List<SchoolInfo>> getSchools();

    boolean isGroupChosen();

    String getGroupId();

    void setGroupId(String groupId);

    void clearGroupId();

    boolean isSchoolChosen();

    int getSchoolId();

    void setSchoolId(int schoolId);

    void clearSchoolId();

    Observable<Group> getChosenGroup();

    Observable<School> getSchool(int schoolId);

    boolean isCachedSchedule();
    String getCachedTime();
    boolean isCacheAvailable();

    Observable<School> getCachedSchool();

    List<Teacher> findTeachers(CharSequence filter);
    List<Group> findGroups(CharSequence filter);
    Single<List<SchoolInfo>> findSchools(CharSequence filter);
}
