package com.grepp.spring.app.model.timer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StudyWeekTimeResponse {

    Long weekTotalStudyTime;

    @Builder
    public StudyWeekTimeResponse(Long totalStudyTime) {
        this.weekTotalStudyTime = totalStudyTime;
    }
}
