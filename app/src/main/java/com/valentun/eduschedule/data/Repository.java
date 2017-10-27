package com.valentun.eduschedule.data;


import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.network.RestService;
import com.valentun.eduschedule.data.persistance.PreferenceManager;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.parser.Parser;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.Lesson;
import com.valentun.parser.pojo.School;
import com.valentun.parser.pojo.Teacher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;

@SuppressWarnings("WeakerAccess")
public class Repository implements IRepository {
    @Inject
    RestService restService;
    @Inject
    OkHttpClient okHttpClient;
    @Inject
    PreferenceManager preferenceManager;
    @Inject
    Parser parser;

    private School school;

    public Repository() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null)
            component.inject(this);
    }

    @Override
    public Observable<School> getSchool() {
        if (school != null) {
            return Observable.just(school);
        } else {
            // TODO replace with network request
            return Observable.just("1")
                    .subscribeOn(Schedulers.io())
                    .map(s -> preferenceManager.getTestData())
                    .map(parser::parseFrom)
                    .doOnNext(result -> this.school = result)
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    @Override
    public Observable<List<Lesson>> getGroupSchedule(String groupId, int dayNumber) {
        return getSchool()
                .map(school1 -> {
                            Group group = school1.getGroup(groupId);
                            return group.getSchedule().get(dayNumber);
                        }
                );
    }

    @Override
    public Observable<List<Lesson>> getTeacherSchedule(String teacherId, int dayNumber) {
        return getSchool()
                .map(school1 -> {
                    Teacher teacher = school1.getTeacher(teacherId);
                    return teacher.getSchedule().get(dayNumber);
                        }
                );
    }

    @Override
    public Observable<List<Group>> getGroups() {
        return getSchool().map(School::getGroups);
    }

    // ======= region selected Group =======

    @Override
    public boolean isGroupChosen() {
        return preferenceManager.isGroupChosen();
    }

    @Override
    public String getGroupId() {
        return preferenceManager.getGroupId();
    }

    @Override
    public void setGroupId(String groupId) {
        preferenceManager.setGroup(groupId);
    }

    @Override
    public void clearGroupId() {
        preferenceManager.clearGroup();
    }

    // end
}
