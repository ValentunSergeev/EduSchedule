package com.valentun.parser.pojo;

import android.util.SparseArray;

import com.valentun.parser.Config;

import java.util.ArrayList;
import java.util.List;

public class Group extends NamedEntity {
    private final SparseArray<List<Lesson>> schedule;

    public Group(String id, String name) {
        super(id, name);
        this.schedule = new SparseArray<>();

        for (int i = 0; i < Config.DAYS_IN_WEEK; i++) {
            schedule.put(i, new ArrayList<>());
        }
    }

    public SparseArray<List<Lesson>> getSchedule() {
        return schedule;
    }
}
