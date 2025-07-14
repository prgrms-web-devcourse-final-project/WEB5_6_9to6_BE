package com.grepp.spring.app.model.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyInfoResponse {

    private Long studyId;
    private String title;
    private int currentMemberCount;
    private int maxMemberCount;
    private String category;
    private String region;
    private String place;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    private List<String> schedules;

    private String startTime;
    private String endTime;

    private String studyType;
}
