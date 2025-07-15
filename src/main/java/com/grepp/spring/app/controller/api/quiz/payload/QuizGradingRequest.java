package com.grepp.spring.app.controller.api.quiz.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class QuizGradingRequest {
    private Long memberId;
    private Long studyId;
    private int week;
    private List<Integer> answerSheet;
}