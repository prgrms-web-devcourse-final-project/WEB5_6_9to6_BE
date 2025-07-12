package com.grepp.spring.app.model.timer.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DailyStudyLogResponse {

    private LocalDate studyDate;
    private Long totalStudyTime;

    public DailyStudyLogResponse(LocalDate studyDate, Long totalStudyTime) {
        this.studyDate = studyDate;
        this.totalStudyTime = totalStudyTime;
    }
}