package com.grepp.studium.goal_achievement.domain;

import com.grepp.studium.study_goal.domain.StudyGoal;
import com.grepp.studium.study_member.domain.StudyMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "GoalAchievements")
@Getter
@Setter
public class GoalAchievement {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer achievementId;

    @Column(nullable = false)
    private Boolean isAccomplished;

    @Column(nullable = false)
    private Boolean activated;

    @Column(nullable = false)
    private LocalDateTime achievedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_member_id", nullable = false)
    private StudyMember studyMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private StudyGoal goal;

}
