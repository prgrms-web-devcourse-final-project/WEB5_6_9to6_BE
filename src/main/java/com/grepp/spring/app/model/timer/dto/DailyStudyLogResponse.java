package com.grepp.spring.app.model.timer.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DailyStudyLogResponse {

    private LocalDate studyDate;
    private Long totalStudySeconds;

    public DailyStudyLogResponse(LocalDate studyDate, Long totalStudySeconds) {
        this.studyDate = studyDate;
        this.totalStudySeconds = totalStudySeconds;
    }
}