package com.grepp.studium.study_applicant.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudyApplicantDTO {

    private Integer applicationId;

    @NotNull
    @Size(max = 255)
    private String state;

    @Size(max = 255)
    private String introduction;

    @NotNull
    private Boolean activated;

    @NotNull
    private Integer member;

    @NotNull
    private Integer study;

}
