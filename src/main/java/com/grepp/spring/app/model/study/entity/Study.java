package com.grepp.spring.app.model.study.entity;

import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.Region;
import com.grepp.spring.app.model.study.code.Status;
import com.grepp.spring.app.model.study.code.StudyType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="study")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
}