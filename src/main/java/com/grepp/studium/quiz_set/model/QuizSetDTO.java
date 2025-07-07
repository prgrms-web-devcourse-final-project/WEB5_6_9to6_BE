package com.grepp.studium.quiz_set.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuizSetDTO {

    private Integer quizSetId;

    @NotNull
    private Integer week;

    @NotNull
    private Boolean activated;

}
