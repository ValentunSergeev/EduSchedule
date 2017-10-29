package com.valentun.eduschedule.data;


import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.dto.SchoolInfo;
import com.valentun.eduschedule.data.network.RestService;
import com.valentun.eduschedule.data.persistance.PreferenceManager;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.parser.Parser;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.Lesson;
import com.valentun.parser.pojo.School;
import com.valentun.parser.pojo.Teacher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressWarnings("WeakerAccess")
public class Repository implements IRepository {
    private static final String SCRIPT_PREFIX = "nika_data";

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
    public Observable<School> getSchool(int schoolId) {
        if (school != null) {
            return Observable.just(school);
        } else {
            return restService.getSchoolInfo(schoolId)
                    .map(this::getPathToData)
                    .map(this::getRawSchool)
                    .map(parser::parseFrom)
                    .doOnNext(result -> this.school = result)
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    @Override
    public Observable<List<Lesson>> getGroupSchedule(String groupId, int dayNumber) {
        return getSchool(getSchoolId())
                .map(school1 -> {
                            Group group = school1.getGroup(groupId);
                            return group.getSchedule().get(dayNumber);
                        }
                );
    }

    @Override
    public Observable<List<Lesson>> getTeacherSchedule(String teacherId, int dayNumber) {
        return getSchool(getSchoolId())
                .map(school1 -> {
                    Teacher teacher = school1.getTeacher(teacherId);
                    return teacher.getSchedule().get(dayNumber);
                        }
                );
    }

    @Override
    public Observable<String> getSchoolName() {
        return getSchool(getSchoolId()).map(School::getName);
    }

    @Override
    public Observable<List<Group>> getGroups() {
        return getSchool(getSchoolId()).map(School::getGroups);
    }

    @Override
    public Observable<List<Teacher>> getTeachers() {
        return getSchool(getSchoolId()).map(School::getTeachers);
    }

    @Override
    public Observable<List<SchoolInfo>> getSchools() {
        return restService.getSchools()
                .observeOn(AndroidSchedulers.mainThread());
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

    // ======= region selected School =======

    @Override
    public boolean isSchoolChosen() {
        return preferenceManager.isSchoolChosen();
    }


    @Override
    public int getSchoolId() {
        return preferenceManager.getSchoolId();
    }

    @Override
    public void setSchoolId(int schoolId) {
        preferenceManager.setSchool(schoolId);
    }


    @Override
    public void clearSchoolId() {
        preferenceManager.clearSchool();
        school = null;
    }

    // end

    private String getRawSchool(String path) throws Exception {
        Response response = okHttpClient.newCall(new Request.Builder()
                .url(path)
                .build())
                .execute();


        if (response.body() != null) {
            return response.body().string();
        } else {
            throw new IOException("Null return result");
        }
    }

    private String getPathToData(SchoolInfo info) throws Exception {
        Document document = Jsoup.connect(info.getPath()).get();

        Elements scriptElements = document.getElementsByTag("script");

        String result = null;

        for (Element script : scriptElements) {
            String src = script.attr("src");

            if (src.startsWith(SCRIPT_PREFIX)) {
                result = normalizePath(info.getDataPath()) + src;
            }
        }

        return result;
    }

    private String normalizePath(String base) {
        if (base.endsWith("/")) {
            return base;
        } else {
            return base + "/";
        }
    }

}
