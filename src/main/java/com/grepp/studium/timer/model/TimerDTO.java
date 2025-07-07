package com.grepp.studium.timer.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TimerDTO {

    private Integer timerId;

    private Integer dailyStudyTime;

    private LocalDateTime createdAt;

    @NotNull
    private Integer studyMember;

}
