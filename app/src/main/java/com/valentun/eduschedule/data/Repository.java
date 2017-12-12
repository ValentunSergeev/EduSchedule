package com.valentun.eduschedule.data;

import android.text.TextUtils;

import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.dto.SchoolInfo;
import com.valentun.eduschedule.data.network.ErrorHandler;
import com.valentun.eduschedule.data.network.NetworkStatusChecker;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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
    private boolean isCachedSchedule = false;

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
            if (NetworkStatusChecker.isNetworkAvailable()) {
                return restService.getSchoolInfo(schoolId)
                        .map(this::getPathToData)
                        .map(this::getRawSchool)
                        .doOnNext(preferenceManager::cacheSchedule)
                        .map(parser::parseFrom)
                        .doOnNext(result -> {
                            this.school = result;
                            isCachedSchedule = false;
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            } else {
                if (preferenceManager.isHasCachedSchedule()) {
                    return Observable.just(schoolId)
                            .subscribeOn(Schedulers.io())
                            .map(id -> {
                                String raw = preferenceManager.getCachedSchedule();
                                return parser.parseFrom(raw);
                            })
                            .doOnNext(result -> {
                                this.school = result;
                                isCachedSchedule = true;
                            })
                            .observeOn(AndroidSchedulers.mainThread());
                } else {
                    return Observable.error(new RuntimeException(ErrorHandler.NO_INTERNET_PREFIX));
                }
            }
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

    // ======= region cached school =======

    public boolean isCachedSchedule() {
        return isCachedSchedule;
    }

    public String getCachedTime() {
        long time = preferenceManager.getCachedTime();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy hh:mm", Locale.getDefault());

        return formatter.format(new Date(time));
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public List<Teacher> findTeachers(CharSequence filter) {
        return getTeachers()
                .flatMap(Observable::fromIterable)
                .filter(item -> {
                    String name = item.getName();

                    if (TextUtils.isEmpty(filter))
                        return true;

                    return name.toLowerCase()
                            .contains(filter.toString().toLowerCase());
                })
                .toList()
                .blockingGet();
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public List<Group> findGroups(CharSequence filter) {
        return getGroups()
                .flatMap(Observable::fromIterable)
                .filter(item -> {
                    String name = item.getName();

                    if (TextUtils.isEmpty(filter))
                        return true;

                    return name.toLowerCase()
                            .contains(filter.toString().toLowerCase());
                })
                .toList()
                .blockingGet();
    }

    @Override
    public Single<List<SchoolInfo>> findSchools(CharSequence filter) {
        return getSchools()
                .flatMap(Observable::fromIterable)
                .filter(item -> {
                    String name = item.getName();

                    if (TextUtils.isEmpty(filter))
                        return true;

                    return name.toLowerCase()
                            .contains(filter.toString().toLowerCase());
                })
                .toList();
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

            if (src.contains(SCRIPT_PREFIX)) {
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
