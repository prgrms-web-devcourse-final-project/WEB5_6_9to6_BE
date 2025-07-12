package com.grepp.spring.app.controller.api.quiz.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SurvivalResultRequest {
    private Long studyMemberId;
    private int correctProblemCount;
    private int totalProblemCount;
    private boolean survived;
    private int week;
}