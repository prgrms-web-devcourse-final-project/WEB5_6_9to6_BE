package com.grepp.spring.app.model.study.dto;

import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.Region;
import com.grepp.spring.app.model.study.code.Status;
import com.grepp.spring.app.model.study.code.StudyType;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class StudyListResponse {

    private final Long studyId;
    private final String title;
    private final Category category;
    private final int currentMemberCount;
    private final int maxMemberCount;
    private final List<String> schedules;
    private final String startTime;
    private final String endTime;
    private final Status status;
    private final String createdAt;
    private final LocalDate startDate;
    private final Region region;
    private final StudyType studyType;
    private final String description;

    @Builder
    public StudyListResponse(Long studyId, String title, Category category, int currentMemberCount,
        int maxMemberCount, List<String> schedules, String startTime, String endTime,
        Status status, String createdAt, LocalDate startDate, Region region,
        StudyType studyType, String description) {
        this.studyId = studyId;
        this.title = title;
        this.category = category;
        this.currentMemberCount = currentMemberCount;
        this.maxMemberCount = maxMemberCount;
        this.schedules = schedules;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = createdAt;
        this.startDate = startDate;
        this.region = region;
        this.studyType = studyType;
        this.description = description;
    }

    public static StudyListResponse fromEntity(Study study, int currentMemberCount, List<StudySchedule> scheduleList) {
        // 요일 리스트 추출
        List<String> days = scheduleList != null
            ? scheduleList.stream()
            .map(s -> s.getDayOfWeek().name())
            .collect(Collectors.toList())
            : Collections.emptyList();

        // 첫 번째 스케줄 기준 시간
        String start = scheduleList != null && !scheduleList.isEmpty()
            ? scheduleList.getFirst().getStartTime()
            : null;

        String end = scheduleList != null && !scheduleList.isEmpty()
            ? scheduleList.getFirst().getEndTime()
            : null;

        return StudyListResponse.builder()
            .studyId(study.getStudyId())
            .title(study.getName())
            .category(study.getCategory())
            .currentMemberCount(currentMemberCount)
            .maxMemberCount(study.getMaxMembers())
            .schedules(days)
            .startTime(start)
            .endTime(end)
            .status(study.getStatus())
            .createdAt(study.getCreatedAt().toLocalDate().format(DateTimeFormatter.ISO_DATE))
            .startDate(study.getStartDate())
            .region(study.getRegion())
            .studyType(study.getStudyType())
            .description(study.getDescription())
            .build();
    }
}