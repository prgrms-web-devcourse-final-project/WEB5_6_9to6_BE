package com.grepp.spring.app.model.study.entity;

import com.grepp.spring.app.model.study.code.GoalType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goalId;

    private String content;

    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    private boolean activated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @OneToMany(mappedBy = "studyGoal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalAchievement> achievements = new ArrayList<>();

    @Builder
    public StudyGoal(String content, GoalType goalType, boolean activated, Study study) {
        this.content = content;
        this.goalType = goalType;
        this.activated = activated;
        this.study = study;
    }

    // 목표 수정
    public void update(String content, GoalType type) {
        this.content = content;
        this.goalType = type;
    }
}
