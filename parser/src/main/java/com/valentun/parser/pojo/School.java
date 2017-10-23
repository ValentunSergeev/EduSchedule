package com.valentun.parser.pojo;

import java.util.ArrayList;
import java.util.List;

public class School {
    private String name;

    private List<Teacher> teachers;
    private List<Group> groups;

    public School(String name) {
        this.name = name;
        teachers = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public Group getGroup(String id) {
        for (Group group : groups) {
            if (group.getId().equals(id))
                return group;
        }
        return null;
    }
}
