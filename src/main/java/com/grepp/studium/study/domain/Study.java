package com.grepp.studium.study.domain;

import com.grepp.studium.study_applicant.domain.StudyApplicant;
import com.grepp.studium.study_goal.domain.StudyGoal;
import com.grepp.studium.study_member.domain.StudyMember;
import com.grepp.studium.study_notice.domain.StudyNotice;
import com.grepp.studium.study_schedule.domain.StudySchedule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Studies")
@Getter
@Setter
public class Study {

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
    private Integer studyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer maxMembers;

    @Column(nullable = false)
    private String region;

    @Column
    private String place;

    @Column(nullable = false)
    private Boolean isOnline;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String status;

    @Column
    private String description;

    @Column
    private String externalLink;

    @Column(nullable = false)
    private String studyType;

    @Column(nullable = false)
    private Boolean activated;

    @OneToMany(mappedBy = "study")
    private Set<StudyMember> studyStudyMembers = new HashSet<>();

    @OneToMany(mappedBy = "study")
    private Set<StudyGoal> studyStudyGoals = new HashSet<>();

    @OneToOne(mappedBy = "study", fetch = FetchType.LAZY)
    private StudyNotice studyStudyNotices;

    @OneToMany(mappedBy = "study")
    private Set<StudyApplicant> studyStudyApplicants = new HashSet<>();

    @OneToMany(mappedBy = "study")
    private Set<StudySchedule> studyUntitleds = new HashSet<>();

}
