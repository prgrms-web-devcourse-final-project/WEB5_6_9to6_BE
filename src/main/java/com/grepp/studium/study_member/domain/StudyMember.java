package com.grepp.studium.study_member.domain;

import com.grepp.studium.attendance.domain.Attendance;
import com.grepp.studium.goal_achievement.domain.GoalAchievement;
import com.grepp.studium.member.domain.Member;
import com.grepp.studium.quiz_record.domain.QuizRecord;
import com.grepp.studium.study.domain.Study;
import com.grepp.studium.timer.domain.Timer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "StudyMembers")
@Getter
@Setter
public class StudyMember {

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
    private Integer studyMemberId;

    @Column(nullable = false)
    private Boolean activated;

    @Column(nullable = false)
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

    @OneToMany(mappedBy = "studyMember")
    private Set<GoalAchievement> studyMemberGoalAchievements = new HashSet<>();

    @OneToMany(mappedBy = "studyMember")
    private Set<Attendance> studyMemberAttendances = new HashSet<>();

    @OneToMany(mappedBy = "studyMember")
    private Set<QuizRecord> studyMemberQuizRecords = new HashSet<>();

    @OneToMany(mappedBy = "studyMember")
    private Set<Timer> studyMemberTimers = new HashSet<>();

}
