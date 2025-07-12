package com.grepp.spring.app.model.timer.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DailyStudyLogResponse {

    private LocalDate studyDate;
    private Long dailyTotalStudyTime;

    public DailyStudyLogResponse(LocalDate studyDate, Long dailyTotalStudyTime) {
        this.studyDate = studyDate;
        this.dailyTotalStudyTime = dailyTotalStudyTime;
    }
}