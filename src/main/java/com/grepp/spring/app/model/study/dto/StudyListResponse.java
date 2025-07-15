package com.grepp.spring.app.model.study.dto;

import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.Region;
import com.grepp.spring.app.model.study.code.Status;
import com.grepp.spring.app.model.study.code.StudyType;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class StudyListResponse {

    private Long studyId;
    private String title;
    private Category category;
    private int currentMemberCount;
    private int maxMemberCount;
    private List<String> schedules;
    private String startTime;
    private String endTime;
    private Status status;
    private String createdAt;
    private LocalDate startDate;
    private Region region;
    private StudyType studyType;

    public static StudyListResponse fromEntity(Study study, int currentMemberCount) {
        List<StudySchedule> scheduleList = study.getSchedules();

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
            .build();
    }
}
