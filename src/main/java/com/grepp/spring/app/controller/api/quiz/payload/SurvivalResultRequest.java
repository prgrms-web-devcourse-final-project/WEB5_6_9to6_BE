package com.grepp.spring.app.controller.api.quiz.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SurvivalResultRequest {
    private Long studyMemberId;
    private int correctProblemCount;
    private int totalProblemCount;
    private boolean isSurvived;
    private int week;
}