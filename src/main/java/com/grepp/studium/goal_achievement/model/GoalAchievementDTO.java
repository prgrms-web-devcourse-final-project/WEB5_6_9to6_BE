package com.grepp.studium.goal_achievement.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GoalAchievementDTO {

    private Integer achievementId;

    @NotNull
    @JsonProperty("isAccomplished")
    private Boolean isAccomplished;

    @NotNull
    private Boolean activated;

    @NotNull
    private LocalDateTime achievedAt;

    @NotNull
    private Integer studyMember;

    @NotNull
    private Integer goal;

}
