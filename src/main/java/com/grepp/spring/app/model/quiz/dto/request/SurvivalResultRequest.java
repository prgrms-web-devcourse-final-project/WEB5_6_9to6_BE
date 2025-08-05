package com.grepp.spring.app.model.quiz.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SurvivalResultRequest {
    private Long studyMemberId;
    @JsonProperty("isSurvived")
    private boolean isSurvived;
}