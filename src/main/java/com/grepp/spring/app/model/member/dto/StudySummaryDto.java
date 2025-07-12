package com.grepp.spring.app.model.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grepp.spring.app.model.study.dto.ScheduleDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudySummaryDto {

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

    private List<ScheduleDto> scheduleList;

    private String studyType;

}
