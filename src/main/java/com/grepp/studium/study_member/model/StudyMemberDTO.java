package com.grepp.studium.study_member.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudyMemberDTO {

    private Integer studyMemberId;

    @NotNull
    private Boolean activated;

    @NotNull
    @Size(max = 255)
    private String role;

    @NotNull
    private Integer member;

    @NotNull
    private Integer study;

}
