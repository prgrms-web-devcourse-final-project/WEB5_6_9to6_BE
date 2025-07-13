package com.grepp.spring.app.model.study.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScheduleDto {
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}

