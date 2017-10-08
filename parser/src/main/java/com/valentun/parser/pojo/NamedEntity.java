package com.valentun.parser.pojo;

class NamedEntity {
    protected final int id;
    protected final String name;

    NamedEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
