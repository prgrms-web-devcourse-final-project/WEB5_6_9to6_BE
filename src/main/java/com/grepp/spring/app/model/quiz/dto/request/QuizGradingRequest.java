package com.grepp.spring.app.model.quiz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizGradingRequest {
    private Long memberId;
    private Long studyId;
    private int week;
    private List<Integer> answerSheet;
}