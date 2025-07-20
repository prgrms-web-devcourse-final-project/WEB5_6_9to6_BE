package com.grepp.spring.app.model.study.dto;

import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.GoalType;
import com.grepp.spring.app.model.study.code.Region;
import com.grepp.spring.app.model.study.code.Status;
import com.grepp.spring.app.model.study.code.StudyType;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.entity.StudyGoal;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class StudyInfoResponse {
    private String name;
    private Category category;
    private int maxMembers;
    private Region region;
    private String place;
    private boolean isOnline;
    private List<String> schedules;
    private String startTime;
    private String endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private String createdAt;
    private Status status;
    private String description;
    private String externalLink;
    private StudyType studyType;
    private List<GoalDTO> goals;
    private String notice;
    private int currentMemberCount;

    @Getter
    @Builder
    public static class GoalDTO {
        private Long goalId;
        private String content;
        private GoalType type;

        public static GoalDTO fromEntity(StudyGoal goal) {
            return GoalDTO.builder()
                .goalId(goal.getGoalId())
                .content(goal.getContent())
                .type(goal.getGoalType())
                .build();
        }
    }

    public static StudyInfoResponse fromEntity(Study study, int currentMemberCount) {
        List<StudySchedule> schedules = study.getSchedules();

        String start = null;
        String end = null;
        if (schedules != null && !schedules.isEmpty()) {
            start = schedules.getFirst().getStartTime();
            end = schedules.getFirst().getEndTime();
        }

        return StudyInfoResponse.builder()
            .name(study.getName())
            .category(study.getCategory())
            .maxMembers(study.getMaxMembers())
            .notice(study.getNotice())
            .currentMemberCount(currentMemberCount)
            .region(study.getRegion())
            .place(study.getPlace())
            .isOnline(study.isOnline())
            .schedules(
                schedules.stream()
                    .map(s -> s.getDayOfWeek().name())
                    .distinct()
                    .collect(Collectors.toList())
            )            .startTime(start)
            .endTime(end)
            .startDate(study.getStartDate())
            .endDate(study.getEndDate())
            .createdAt(study.getCreatedAt().toLocalDate().toString())
            .status(study.getStatus())
            .description(study.getDescription())
            .externalLink(study.getExternalLink())
            .studyType(study.getStudyType())
            .goals(
                study.getGoals().stream()
                    .filter(StudyGoal::isActivated)
                    .map(GoalDTO::fromEntity)
                    .collect(Collectors.toList())
            )
            .build();
    }

    private static String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
