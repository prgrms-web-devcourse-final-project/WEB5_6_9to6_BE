package com.grepp.spring.app.controller.api.study.payload;

import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.DayOfWeek;
import com.grepp.spring.app.model.study.code.GoalType;
import com.grepp.spring.app.model.study.code.Region;
import com.grepp.spring.app.model.study.code.StudyType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class StudyCreationRequest {
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
    private String description;
    private String externalLink;
    private StudyType studyType;
    private List<GoalDTO> goals;

    @Getter
    public static class GoalDTO {
        private Long goalId;
        private String content;
        private GoalType type;
    }
}
