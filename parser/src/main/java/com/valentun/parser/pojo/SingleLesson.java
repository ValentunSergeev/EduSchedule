package com.valentun.parser.pojo;

public class SingleLesson extends Lesson {
    private Teacher teacher;
    private NamedEntity room;
    private NamedEntity subject;

    public Teacher getTeacher() {
        return teacher;
    }

    public NamedEntity getRoom() {
        return room;
    }

    public NamedEntity getSubject() {
        return subject;
    }

    public SingleLesson setTeacher(Teacher teacher) {
        this.teacher = teacher;
        return this;
    }

    public SingleLesson setRoom(NamedEntity room) {
        this.room = room;
        return this;
    }

    public SingleLesson setSubject(NamedEntity subject) {
        this.subject = subject;
        return this;
    }
}
