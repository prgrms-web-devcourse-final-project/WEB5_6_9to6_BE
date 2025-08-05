package com.grepp.spring.app.model.study.entity;

import com.grepp.spring.infra.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalAchievement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long achievementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private StudyGoal studyGoal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_member_id")
    private StudyMember studyMember;

    private boolean isAccomplished;

    private LocalDateTime achievedAt;

    @Builder
    public GoalAchievement(StudyGoal studyGoal, StudyMember studyMember,
        boolean isAccomplished, LocalDateTime achievedAt) {
        this.studyGoal = studyGoal;
        this.studyMember = studyMember;
        this.isAccomplished = isAccomplished;
        this.achievedAt = achievedAt;
    }
}
