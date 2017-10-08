package com.valentun.parser.pojo;

import android.util.SparseArray;

import java.util.List;

public class Teacher extends NamedEntity {
    private final SparseArray<List<Lesson>> schedule;

    public Teacher(int id, String name) {
        super(id, name);
        this.schedule = new SparseArray<>();
    }

    public SparseArray<List<Lesson>> getSchedule() {
        return schedule;
    }
}
