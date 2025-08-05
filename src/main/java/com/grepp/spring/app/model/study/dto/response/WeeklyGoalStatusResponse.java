package com.grepp.spring.app.model.study.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WeeklyGoalStatusResponse {

    private Long studyId;
    private List<GoalStat> goals;

    @Getter
    @AllArgsConstructor
    public static class GoalStat {
        private String week;
        private Long count;
    }
}

