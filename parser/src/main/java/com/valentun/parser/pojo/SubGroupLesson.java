package com.valentun.parser.pojo;

import java.util.HashMap;
import java.util.Map;

public class SubGroupLesson extends Lesson {
    private Map<NamedEntity, SingleLesson> subLessons = new HashMap<>();

    public Map<NamedEntity, SingleLesson> getSubLessons() {
        return subLessons;
    }

    public SubGroupLesson addSubLesson(NamedEntity subGroup, SingleLesson lesson) {
        subLessons.put(subGroup, lesson);
        return this;
    }
}
