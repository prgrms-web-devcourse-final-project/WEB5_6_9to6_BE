package com.grepp.spring.app.controller.api.study.payload;

import lombok.Getter;

@Getter
public class WeeklyAchievementCount {

    private final String week;
    private final Long count;

    public WeeklyAchievementCount(Object week, Long count) {
        this.week = String.valueOf(week);
        this.count = count;
    }

}
