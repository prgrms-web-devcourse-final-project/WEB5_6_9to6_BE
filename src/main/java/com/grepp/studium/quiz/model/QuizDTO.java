package com.grepp.studium.quiz.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuizDTO {

    private Integer quizId;

    @Size(max = 255)
    private String question;

    @Size(max = 255)
    private String answer;

    @NotNull
    private Boolean activated;

    @NotNull
    private Integer quizSet;

}
