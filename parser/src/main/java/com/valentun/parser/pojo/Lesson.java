package com.valentun.parser.pojo;

@SuppressWarnings("unchecked")
public abstract class Lesson {
    protected Period period;
    protected Group group;

    public boolean isSingle() {
        return this instanceof SingleLesson;
    }

    public boolean isSubGroup() {
        return this instanceof SubGroupLesson;
    }

    public <T extends Lesson>  T asSub() {
        return (T) this;
    }

    public Period getPeriod() {
        return period;
    }

    public Lesson setPeriod(Period period) {
        this.period = period;
        return this;
    }

    public Group getGroup() {
        return group;
    }

    public Lesson setGroup(Group group) {
        this.group = group;
        return this;
    }
}
