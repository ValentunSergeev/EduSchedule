package com.valentun.eduschedule.data;

import android.text.TextUtils;

import com.valentun.eduschedule.BuildConfig;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.data.dto.SchoolInfo;
import com.valentun.eduschedule.data.network.ErrorHandler;
import com.valentun.eduschedule.data.network.NetworkStatusChecker;
import com.valentun.eduschedule.data.network.RestService;
import com.valentun.eduschedule.data.persistance.PreferenceManager;
import com.valentun.eduschedule.data.persistance.SettingsManager;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.jobs.JobManager;
import com.valentun.parser.Parser;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.Lesson;
import com.valentun.parser.pojo.NamedEntity;
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
    @Inject
    JobManager jobManager;
    @Inject
    SettingsManager settingsManager;

    private School school;
    private boolean isCachedSchedule = false;

    public Repository() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null)
            component.inject(this);
    }

    @Override
    public Observable<School> getSchool(int schoolId, boolean forceUpdate) {
        if (school != null && !forceUpdate) {
            return Observable.just(school);
        } else {
            if (NetworkStatusChecker.isNetworkAvailable()) {
                return restService.getSchoolInfo(schoolId)
                        .map(info -> {
                            String path = getPathToData(info);
                            String rawData = getRawSchool(path);

                            School result = parser.parseFrom(rawData);

                            preferenceManager.cacheSchedule(rawData);
                            preferenceManager.savePath(path);

                            this.school = result;
                            isCachedSchedule = false;

                            if (settingsManager.isNotificationsEnabled())
                                jobManager.startJob(Constants.JOBS.CHECK_SCHEDULE);

                            return result;
                        })
                        .observeOn(AndroidSchedulers.mainThread());
            } else {
                if (preferenceManager.isHasCachedSchedule()) {
                    return getCachedSchool();
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
    public boolean isObjectChosen() {
        return preferenceManager.isObjectChosen();
    }

    @Override
    public String getObjectId() {
        return preferenceManager.getObjectId();
    }

    @Override
    public void setMyScheduleObjectId(String objectId) {
        preferenceManager.setMyScheduleObject(objectId);
    }

    @Override
    public void clearMyScheduleObjectId() {
        preferenceManager.clearMyScheduleObject();
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

        jobManager.stopJob(Constants.JOBS.CHECK_SCHEDULE);
    }

    @Override
    public Observable<NamedEntity> getChosenScheduleObject() {
        Observable<NamedEntity> observable;

        switch (settingsManager.getPreferredScheduleType()) {
            case Constants.TYPE_STUDENT:
                observable = getSchool(getSchoolId())
                        .map(school1 -> (NamedEntity) school1.getGroup(String.valueOf(getObjectId())));
                break;
            case Constants.TYPE_TEACHER:
                observable = getSchool(getSchoolId())
                        .map(school1 -> (NamedEntity) school1.getTeacher(String.valueOf(getObjectId())));
                break;
            default:
                throw new IllegalArgumentException("Unknown type");
        }

        return observable.observeOn(AndroidSchedulers.mainThread());
    }

    // end

    // ======= region cached school =======

    @Override
    public boolean isCachedSchedule() {
        return isCachedSchedule;
    }

    @Override
    public String getCachedTime() {
        long time = preferenceManager.getCachedTime();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy hh:mm", Locale.getDefault());

        return formatter.format(new Date(time));
    }

    @Override
    public boolean isCacheAvailable() {
        return preferenceManager.isHasCachedSchedule();
    }

    @Override
    public Observable<School> getCachedSchool() {
        return Observable.just(0)
                .subscribeOn(Schedulers.io())
                .map(id -> {
                    String raw = preferenceManager.getCachedSchedule();
                    return parser.parseFrom(raw);
                })
                .doOnNext(result -> {
                    this.school = result;
                    isCachedSchedule = true;
                })
                .doOnError(error -> {
                    // cached version is not a valid json, so delete it
                    preferenceManager.clearCachedSchedule();
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    // end

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

    @SuppressWarnings("SimplifiableIfStatement")
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

    @Override
    public Observable<Boolean> checkScheduleChangedAndUpdate() {
        return restService.getSchoolInfo(preferenceManager.getSchoolId())
                .map(schoolInfo -> {
                    String savedPath = preferenceManager.getSavedPath();
                    String actualPath = getPathToData(schoolInfo);

                    boolean isChanged = !actualPath.equals(savedPath);

                    if (isChanged) {
                        preferenceManager.savePath(actualPath);
                    }

                    return isChanged || BuildConfig.DEBUG_NOTIFICATIONS;
                });
    }

    private Observable<School> getSchool(int schoolId) {
        return getSchool(schoolId, false);
    }

    @SuppressWarnings("ConstantConditions")
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
