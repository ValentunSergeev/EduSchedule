package com.valentun.eduschedule.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchoolInfo {
    private int id;

    private String name;
    private String path;
    @JsonProperty("data_path")
    private String dataPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }
}
