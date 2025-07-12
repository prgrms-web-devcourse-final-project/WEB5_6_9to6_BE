package com.grepp.spring.app.model.study.entity;

import com.grepp.spring.app.model.member.entity.StudyMember;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalAchievement {

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

    private boolean activated;

    private LocalDateTime achievedAt;

    @Builder
    public GoalAchievement(StudyGoal studyGoal, StudyMember studyMember,
        boolean isAccomplished, boolean activated,
        LocalDateTime achievedAt) {
        this.studyGoal = studyGoal;
        this.studyMember = studyMember;
        this.isAccomplished = isAccomplished;
        this.activated = activated;
        this.achievedAt = achievedAt;
    }
}
