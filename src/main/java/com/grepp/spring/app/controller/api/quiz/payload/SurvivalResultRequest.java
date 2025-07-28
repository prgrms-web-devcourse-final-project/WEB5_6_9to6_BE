package com.grepp.spring.app.controller.api.quiz.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SurvivalResultRequest {
    private Long studyMemberId;
    private boolean isSurvived;
}