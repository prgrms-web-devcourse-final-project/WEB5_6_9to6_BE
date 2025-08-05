package com.grepp.spring.app.model.quiz.dto.response;

import com.grepp.spring.app.model.quiz.dto.internal.QuizDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class QuizListResponse {

    private int week;
    private List<QuizDto> quizzes;
}