package com.grepp.spring.app.model.study.code;

public enum Category {
    ALL("전체"),
    LANGUAGE("어학"),
    JOB("취업"),
    PROGRAMMING("프로그래밍"),
    EXAM_PUBLIC("고시&공무원"),
    EXAM_SCHOOL("수능&내신"),
    ETC("기타");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

