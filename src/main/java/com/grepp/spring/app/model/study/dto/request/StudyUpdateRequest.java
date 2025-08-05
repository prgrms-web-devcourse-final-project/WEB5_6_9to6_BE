package com.grepp.spring.app.model.study.dto.request;

import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.DayOfWeek;
import com.grepp.spring.app.model.study.code.GoalType;
import com.grepp.spring.app.model.study.code.Region;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class StudyUpdateRequest {
    private String name;
    private Category category;
    private int maxMembers;
    private Region region;
    private String place;
    private boolean isOnline;
    private List<DayOfWeek> schedules;
    private String startTime;
    private String endTime;
    private LocalDate endDate;
    private String description;
    private String externalLink;
    private List<GoalUpdateDTO> goals;

    @Getter
    @Setter
    public static class GoalUpdateDTO {
        private Long goalId;
        private String content;
        private GoalType type;
    }
}
