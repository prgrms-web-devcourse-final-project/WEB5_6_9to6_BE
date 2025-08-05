package com.grepp.spring.app.model.quiz.dto.maybeResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizGenerationDto {
    private String question;
    private List<String> choices;
    private int answer;
}