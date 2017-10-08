package com.valentun.parser.pojo;

public class Lesson {
    private Teacher teacher;
    private NamedEntity room;
    private NamedEntity subject;
    private Group group;
    private Period period;

    public Teacher getTeacher() {
        return teacher;
    }

    public NamedEntity getRoom() {
        return room;
    }

    public Group getGroup() {
        return group;
    }

    public Period getPeriod() {
        return period;
    }

    public NamedEntity getSubject() {
        return subject;
    }

    public class Builder {
        private Lesson lesson;

        public Builder() {
            lesson = new Lesson();
        }

        public Builder setTeacher(Teacher teacher) {
            lesson.teacher = teacher;
            return this;
        }

        public Builder setRoom(NamedEntity room) {
            lesson.room = room;
            return this;
        }

        public Builder setGroup(Group group) {
            lesson.group = group;
            return this;
        }

        public Builder setPeriod(Period period) {
            lesson.period = period;
            return this;
        }

        public Builder setSubject(NamedEntity subject) {
            lesson.subject = subject;
            return this;
        }

        public Lesson build() {
            return lesson;
        }
    }
}
