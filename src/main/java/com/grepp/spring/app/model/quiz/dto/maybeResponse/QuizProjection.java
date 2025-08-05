package com.grepp.spring.app.model.quiz.dto.maybeResponse;

import lombok.Getter;
import java.util.List;

@Getter
public class QuizProjection {
    private final int week;
    private final Long quizId;
    private final String question;
    private final List<String> choices;
    private final Integer answer;

    public QuizProjection(int week, Long quizId, String question,
                          String choice1, String choice2, String choice3, String choice4,
                          Integer answer) {
        this.week = week;
        this.quizId = quizId;
        this.question = question;
        this.choices = List.of(choice1, choice2, choice3, choice4);
        this.answer = answer;
    }
}