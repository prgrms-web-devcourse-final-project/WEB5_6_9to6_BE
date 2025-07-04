package com.grepp.studium.study_goal.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudyGoalDTO {

    private Integer goalId;

    @NotNull
    private String content;

    @NotNull
    @Size(max = 255)
    private String type;

    @NotNull
    private Boolean activated;

    @NotNull
    private Integer study;

}
