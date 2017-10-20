package com.valentun.parser.pojo;

public class NamedEntity {
    protected final String id;
    protected final String name;

    public NamedEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public int getIntId() {
        return Integer.parseInt(id);
    }

    public String getName() {
        return name;
    }
}
