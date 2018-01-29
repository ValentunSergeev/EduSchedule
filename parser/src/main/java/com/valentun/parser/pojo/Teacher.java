package com.valentun.parser.pojo;

import android.util.SparseArray;

import com.valentun.parser.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Teacher extends NamedEntity {
    private volatile SparseArray<List<Lesson>> schedule;

    public Teacher(String id, String name) {
        super(id, name);
        this.schedule = new SparseArray<>();

        for (int i = 0; i < Config.DAYS_IN_WEEK; i++) {
            schedule.put(i, Collections.synchronizedList(new ArrayList<>()));
        }
    }

    public SparseArray<List<Lesson>> getSchedule() {
        return schedule;
    }
}
