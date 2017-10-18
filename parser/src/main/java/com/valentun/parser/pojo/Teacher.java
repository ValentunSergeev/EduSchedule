package com.valentun.parser.pojo;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends NamedEntity {
    private final SparseArray<List<Lesson>> schedule;

    public Teacher(String id, String name) {
        super(id, name);
        this.schedule = new SparseArray<>();

        for (int i = 0; i < 6; i++) {
            schedule.put(i, new ArrayList<>());
        }
    }

    public SparseArray<List<Lesson>> getSchedule() {
        return schedule;
    }
}
