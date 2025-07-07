package com.grepp.studium.attendance.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AttendanceDTO {

    private Integer attendanceId;

    @NotNull
    private LocalDate attendanceDate;

    @NotNull
    @JsonProperty("isAttended")
    private Boolean isAttended;

    @NotNull
    private Boolean activated;

    @NotNull
    private Integer studyMember;

}
