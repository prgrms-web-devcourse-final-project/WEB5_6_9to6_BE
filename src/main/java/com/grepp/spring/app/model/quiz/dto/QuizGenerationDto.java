package com.grepp.spring.app.model.quiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class QuizGenerationDto {
    private String question;
    private List<String> choices;
    private int answer;
}