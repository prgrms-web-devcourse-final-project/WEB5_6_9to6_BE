package com.grepp.spring.app.model.study.entity;

import com.grepp.spring.app.model.study.code.GoalType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_goal")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
}
