package com.grepp.spring.app.model.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class QuizGenerationDto {
    private String question;
    private List<String> choices;
    private int answer;
}