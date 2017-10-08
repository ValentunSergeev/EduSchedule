package com.valentun.parser;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.School;
import com.valentun.parser.pojo.Teacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Parser {
    private static final String LOG_TAG = "ScheduleParser";

    private boolean isLogEnabled = true;
    private List<Teacher> teachers;
    private List<Group> groups;

    public void setLogEnabled(boolean logEnabled) {
        isLogEnabled = logEnabled;
    }

    public Observable<School> parseFrom(String rawData) {
        return Observable.just(rawData)
                .subscribeOn(Schedulers.io())
                .map(this::parse)
                .observeOn(AndroidSchedulers.mainThread());
    }

    private School parse(String data) throws IOException {
        long start = System.currentTimeMillis();

        String json = getRawJson(data);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        teachers = parseTeachers(root);
        groups = parseGroups(root);

        School school = createSchool(root);

        long end = System.currentTimeMillis();
        log(String.valueOf((end - start) / 1000.0));
        return school;
    }

    private School createSchool(JsonNode root) {
        String name = getSchoolName(root);

        School school = new School(name);
        school.getTeachers().addAll(teachers);
        school.getGroups().addAll(groups);

        return school;
    }

    private List<Group> parseGroups(JsonNode root) {
        List<Group> groups = new ArrayList<>();

        JsonNode teachersNode = root.get(Config.GROUPS_KEY);

        Iterator<Entry<String, JsonNode>> iterator = teachersNode.fields();

        while (iterator.hasNext()) {
            Entry<String, JsonNode> entry = iterator.next();

            String name = entry.getValue().asText();
            int id = Integer.parseInt(entry.getKey());

            groups.add(new Group(id, name));
        }

        return groups;
    }

    private String getSchoolName(JsonNode root) {
        return root.get(Config.SCHOOL_NAME).asText();
    }

    private List<Teacher> parseTeachers(JsonNode root) {
        List<Teacher> teachers = new ArrayList<>();

        JsonNode teachersNode = root.get(Config.TEACHERS_KEY);

        Iterator<Entry<String, JsonNode>> iterator = teachersNode.fields();

        while (iterator.hasNext()) {
            Entry<String, JsonNode> entry = iterator.next();

            String name = entry.getValue().asText();
            int id = Integer.parseInt(entry.getKey());

            teachers.add(new Teacher(id, name));
        }

        return teachers;
    }

    private String getRawJson(String rawData) {
        int index = rawData.indexOf('{');
        return rawData.substring(index);
    }

    private void log(String s) {
        if (isLogEnabled) Log.d(LOG_TAG, s);
    }

}
