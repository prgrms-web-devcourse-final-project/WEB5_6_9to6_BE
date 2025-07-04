package com.grepp.studium.quiz_record.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class QuizRecordDTO {

    private Integer quizRecordId;

    @NotNull
    @JsonProperty("isPassed")
    private Boolean isPassed;

    @NotNull
    private Boolean activated;

    @NotNull
    private Integer studyMember;

    @NotNull
    private Integer quizSet;

}
