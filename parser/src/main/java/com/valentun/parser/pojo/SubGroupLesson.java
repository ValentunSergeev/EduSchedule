package com.valentun.parser.pojo;

import java.util.ArrayList;
import java.util.List;

public class SubGroupLesson extends Lesson {
    private List<SingleLesson> lessons = new ArrayList<>();
    private List<NamedEntity> subGroups = new ArrayList<>();

    public List<SingleLesson> getSubLessons() {
        return lessons;
    }

    public List<NamedEntity> getSubGroups() {
        return subGroups;
    }

    public SubGroupLesson addSubLesson(NamedEntity subGroup, SingleLesson lesson) {
        lessons.add(lesson);
        subGroups.add(subGroup);
        return this;
    }
}
