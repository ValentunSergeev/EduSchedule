package com.valentun.parser;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.valentun.parser.pojo.Group;
import com.valentun.parser.pojo.Lesson;
import com.valentun.parser.pojo.NamedEntity;
import com.valentun.parser.pojo.Period;
import com.valentun.parser.pojo.School;
import com.valentun.parser.pojo.SingleLesson;
import com.valentun.parser.pojo.SubGroupLesson;
import com.valentun.parser.pojo.Teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Parser {
    private static final String LOG_TAG = "ScheduleParser";

    // helper vars
    private boolean isLogEnabled = true;
    private long parseTime = 0;

    // data vars
    private List<Teacher> teachers;
    private List<Group> groups;
    private List<NamedEntity> subjects;
    private List<NamedEntity> rooms;
    private List<NamedEntity> subGroups;
    private List<Period> periods;

    private JsonNode root;

    public long getParseTime() {
        if (parseTime == 0)
            throw new RuntimeException("Method called before parsing process has been completed");

        return parseTime;
    }

    public void setLogEnabled(boolean logEnabled) {
        isLogEnabled = logEnabled;
    }

    public School parseFrom(String rawData) throws Exception {
        return parse(rawData);
    }

    private School parse(String data) throws Exception {
        long start = System.currentTimeMillis();

        String json = getRawJson(data);

        ObjectMapper mapper = new ObjectMapper();

        root = mapper.readTree(json);

        teachers = parseNamedEntity(Teacher.class, Config.TEACHERS_KEY);
        groups = parseNamedEntity(Group.class, Config.GROUPS_KEY);
        subjects = parseNamedEntity(NamedEntity.class, Config.LESSONS_KEY);
        rooms = parseNamedEntity(NamedEntity.class, Config.ROOMS_KEY);
        subGroups = parseNamedEntity(NamedEntity.class, Config.CLASSGROUPS_KEY);

        periods = parsePeriods();

        parseSchedule();

        sortTeachersSchedule();

        School school = createSchool();

        long end = System.currentTimeMillis();

        parseTime = end - start;

        log(String.valueOf(parseTime / 1000.0));
        return school;
    }

    private void sortTeachersSchedule() {
        for (Teacher teacher : teachers) {
            for (int i = 0; i < teacher.getSchedule().size(); i++) {
                List<Lesson> lessons = teacher.getSchedule().get(i);

                Collections.sort(lessons, (lesson1, lesson2) -> {
                    int id1 = lesson1.getPeriod().getId();
                    int id2 = lesson2.getPeriod().getId();
                    if (id1 > id2)
                        return 1;
                    else if (id2 > id1)
                        return -1;
                    else return 0;
                });
            }
        }
    }

    private List<Period> parsePeriods() {
        List<Period> result = new ArrayList<>();

        Iterator<Entry<String, JsonNode>> periodIterator = root.get(Config.PERIODS_KEY).fields();

        while (periodIterator.hasNext()) {
            Entry<String, JsonNode> entry = periodIterator.next();

            int id = Integer.parseInt(entry.getKey());
            ArrayNode times = (ArrayNode) entry.getValue();
            String start = times.get(0).asText();
            String end = times.get(1).asText();

            result.add(new Period(id, start, end));
        }

        return result;
    }

    private void parseSchedule() {
        Iterator<Entry<String, JsonNode>> groupIterator = root
                .get(Config.CLASS_SCHEDULE)
                .fields()
                .next()
                .getValue()
                .fields();

        while (groupIterator.hasNext()) {
            Entry<String, JsonNode> entry = groupIterator.next();

            parseGroupSchedule(entry);
        }
    }

    private School createSchool() {
        String name = getSchoolName();

        School school = new School(name);
        school.getTeachers().addAll(teachers);
        school.getGroups().addAll(groups);

        return school;
    }

    private void parseGroupSchedule(Entry<String, JsonNode> groupEntry) {
        Group group = findGroup(groupEntry.getKey());

        Iterator<Entry<String, JsonNode>> lessonIterator = groupEntry.getValue().fields();

        while (lessonIterator.hasNext()) {
            Entry<String, JsonNode> lessonEntry = lessonIterator.next();

            parseLesson(lessonEntry, group);
        }
    }

    @SuppressWarnings("Convert2streamapi")
    private void parseLesson(Entry<String, JsonNode> lessonEntry, Group group) {
        JsonNode value = lessonEntry.getValue();

        String key = lessonEntry.getKey();
        String dayNumber = key.substring(0, 1);
        int periodId = Integer.parseInt(key.substring(1));

        Period period = findPeriod(periodId);

        JsonNode subjects = value.get(Config.LESSON_SUBJECT);

        Lesson lesson;

        int dayNumberInt = Integer.parseInt(dayNumber);

        if (subjects.size() > 1) {
            lesson = parseSubGroupLesson(value, dayNumberInt);
        } else {
            lesson = parseSingleLesson(value, dayNumberInt);
        }

        lesson.setGroup(group)
                .setPeriod(period);

        if (lesson.isSubGroup()) {
            Collection<SingleLesson> subLessons = lesson.<SubGroupLesson>asSub().getSubLessons();
            for (SingleLesson subLesson : subLessons) {
                if (subLesson != null) {
                    subLesson.setGroup(group)
                            .setPeriod(period);
                }
            }
        }
        group.getSchedule().get(dayNumberInt - 1).add(lesson);
    }

    private Lesson parseSingleLesson(JsonNode rootElement, int dayNumber) {
        String subjectId = rootElement.get(Config.LESSON_SUBJECT).get(0).asText();
        NamedEntity subject = findSubject(subjectId);

        String roomId = rootElement.get(Config.LESSON_ROOM).get(0).asText();
        NamedEntity room = findRoom(roomId);

        String teacherId = rootElement.get(Config.LESSON_TEACHER).get(0).asText();
        Teacher teacher = findTeacher(teacherId);

        Lesson lesson = new SingleLesson()
                .setSubject(subject)
                .setTeacher(teacher)
                .setRoom(room);

        teacher.getSchedule().get(dayNumber - 1).add(lesson);
        return lesson;
    }

    private Lesson parseSubGroupLesson(JsonNode rootElement, int dayNumber) {
        JsonNode subjectsNode = rootElement.get(Config.LESSON_SUBJECT);
        JsonNode roomsNode = rootElement.get(Config.LESSON_ROOM);
        JsonNode subGroupsNode = rootElement.get(Config.LESSON_SUBGROUP);
        JsonNode teachersNode = rootElement.get(Config.LESSON_TEACHER);

        SubGroupLesson lesson = new SubGroupLesson();

        for (int i = 0; i < subjectsNode.size(); i++) {
            String subGroupId = subGroupsNode.get(i).asText();
            NamedEntity subGroup = findSubGroup(subGroupId);

            String subjectId = subjectsNode.get(i).asText();

            if (subjectId.equals(Config.EMPTY_FIELD)) {
                lesson.addSubLesson(subGroup, null);
                continue;
            }

            NamedEntity subject = findSubject(subjectId);

            String teacherId = teachersNode.get(i).asText();
            Teacher teacher = findTeacher(teacherId);

            String roomId = roomsNode.get(i).asText();
            NamedEntity room = findRoom(roomId);

            SingleLesson subLesson = new SingleLesson()
                    .setSubject(subject)
                    .setTeacher(teacher)
                    .setRoom(room);

            lesson.addSubLesson(subGroup, subLesson);

            teacher.getSchedule().get(dayNumber - 1).add(subLesson);
        }

        return lesson;
    }

    private NamedEntity findSubGroup(String subGroupId) {
        for (NamedEntity subject : subGroups) {
            if (subject.getId().equals(subGroupId))
                return subject;
        }
        return null;
    }

    private NamedEntity findRoom(String roomId) {
        for (NamedEntity subject : rooms) {
            if (subject.getId().equals(roomId))
                return subject;
        }
        return null;
    }

    private Teacher findTeacher(String teacherId) {
        for (Teacher teacher : teachers) {
            if (teacher.getIntId() == Integer.parseInt(teacherId))
                return teacher;
        }
        return null;
    }

    private NamedEntity findSubject(String subjectId) {
        for (NamedEntity subject : subjects) {
            if (subject.getId().equals(subjectId))
                return subject;
        }
        return null;
    }

    private Period findPeriod(int periodId) {
        for (Period period : periods) {
            if (period.getId() == periodId)
                return period;
        }
        return null;
    }

    private Group findGroup(String key) {
        for (Group group : groups) {
            if (group.getId().equals(key))
                return group;
        }
        return null;
    }

    private String getSchoolName() {
        return root.get(Config.SCHOOL_NAME).asText();
    }


    private <T extends NamedEntity> List<T> parseNamedEntity(Class<T> tClass, String key) throws Exception {
        List<T> result = new ArrayList<>();

        JsonNode childNode = root.get(key);

        Iterator<Entry<String, JsonNode>> iterator = childNode.fields();

        while (iterator.hasNext()) {
            Entry<String, JsonNode> entry = iterator.next();

            String name = entry.getValue().asText();
            String id = entry.getKey();

            result.add(tClass
                    .getConstructor(String.class, String.class)
                    .newInstance(id, name));
        }

        return result;
    }

    private String getRawJson(String rawData) {
        int index = rawData.indexOf('{');
        return rawData.substring(index);
    }

    private void log(String s) {
        if (isLogEnabled) Log.d(LOG_TAG, s);
    }

}
