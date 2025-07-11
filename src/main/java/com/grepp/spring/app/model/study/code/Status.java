package com.grepp.spring.app.model.study.code;

public enum Status {
    ALL("전체"),
    READY("활동준비"),
    ACTIVE("활동중");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
