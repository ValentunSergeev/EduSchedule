package com.valentun.parser.pojo;

public class Period {
    private final int id;
    private final String startTime;
    private final String endTime;

    public Period(int id, String startTime, String endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
