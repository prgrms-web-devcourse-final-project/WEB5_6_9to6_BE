package com.grepp.studium.study.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudyDTO {

    private Integer studyId;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String category;

    @NotNull
    private Integer maxMembers;

    @NotNull
    @Size(max = 255)
    private String region;

    @Size(max = 255)
    private String place;

    @NotNull
    @JsonProperty("isOnline")
    private Boolean isOnline;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    @Size(max = 255)
    private String status;

    @Size(max = 255)
    private String description;

    @Size(max = 255)
    private String externalLink;

    @NotNull
    @Size(max = 255)
    private String studyType;

    @NotNull
    private Boolean activated;

}
