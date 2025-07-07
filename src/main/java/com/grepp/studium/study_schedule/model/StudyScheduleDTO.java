package com.grepp.studium.study_schedule.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudyScheduleDTO {

    @Size(max = 255)
    @StudyScheduleIdValid
    private String id;

    @Size(max = 255)
    private String dayOfWeek;

    @Size(max = 255)
    private String startTime;

    @Size(max = 255)
    private String endTime;

    @NotNull
    private Integer study;

}
