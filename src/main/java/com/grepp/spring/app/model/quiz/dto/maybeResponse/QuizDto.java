package com.grepp.spring.app.model.quiz.dto.maybeResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class QuizDto {
    private Long quizId;
    private String question;
    private List<String> choices;
    private Integer answer;
}