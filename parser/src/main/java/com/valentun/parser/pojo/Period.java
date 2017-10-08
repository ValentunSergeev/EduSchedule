package com.valentun.parser.pojo;

public class Period {
    private final int lessonNumber;
    private final String startTime;
    private final String endTime;

    public Period(int lessonNumber, String startTime, String endTime) {
        this.lessonNumber = lessonNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
