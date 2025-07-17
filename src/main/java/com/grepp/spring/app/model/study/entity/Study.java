package com.grepp.spring.app.model.study.entity;

import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.DayOfWeek;
import com.grepp.spring.app.model.study.code.Region;
import com.grepp.spring.app.model.study.code.Status;
import com.grepp.spring.app.model.study.code.StudyType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    private int maxMembers;

    @Enumerated(EnumType.STRING)
    private Region region;

    private String place;

    private boolean isOnline;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String notice;

    private String description;

    private String externalLink;

    @Enumerated(EnumType.STRING)
    private StudyType studyType;

    private boolean activated;

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudyGoal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudySchedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL)
    private List<StudyMember> studyMembers = new ArrayList<>();

    @Builder
    public Study(String name, Category category, int maxMembers, Region region,
        String place, boolean isOnline, LocalDate startDate, LocalDate endDate,
        LocalDateTime createdAt, Status status, String notice, String description,
        String externalLink, StudyType studyType, boolean activated) {
        this.name = name;
        this.category = category;
        this.maxMembers = maxMembers;
        this.region = region;
        this.place = place;
        this.isOnline = isOnline;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
        this.status = status;
        this.notice = notice;
        this.description = description;
        this.externalLink = externalLink;
        this.studyType = studyType;
        this.activated = activated;
    }

    // 스터디 정보 수정
    public void updateBaseInfo(String name, Category category, int maxMembers,
        Region region, String place, boolean isOnline,
        String description, String externalLink
    ) {
        this.name = name;
        this.category = category;
        this.maxMembers = maxMembers;
        this.region = region;
        this.place = place;
        this.isOnline = isOnline;
        this.description = description;
        this.externalLink = externalLink;
//        this.status = status;
    }

    // 스터디 일정 추가
    public void addSchedule(DayOfWeek dayOfWeek, String start, String end) {
        this.schedules.add(
            new StudySchedule(dayOfWeek, start, end, this)
        );
    }

    // endDate 수정
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void addGoal(StudyGoal goal) {
        this.getGoals().add(goal);
        goal.setStudy(this);
    }

    public Study(Long studyId) {
        this.studyId = studyId;
    }
  
    public void updateNotice(String notice) {
        this.notice = notice;
    }

}