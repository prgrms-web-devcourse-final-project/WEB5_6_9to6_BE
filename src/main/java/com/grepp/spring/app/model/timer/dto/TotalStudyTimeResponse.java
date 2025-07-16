package com.grepp.spring.app.model.timer.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TotalStudyTimeResponse {

    Long totalStudyTime;

    @Builder
    public TotalStudyTimeResponse(Long totalStudyTime) {
        this.totalStudyTime = totalStudyTime;
    }
}
