package com.grepp.studium.member.domain;

import com.grepp.studium.alarm_recipient.domain.AlarmRecipient;
import com.grepp.studium.own_item.domain.OwnItem;
import com.grepp.studium.study_applicant.domain.StudyApplicant;
import com.grepp.studium.study_member.domain.StudyMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "Members")
@Getter
@Setter
public class Member {

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
    private Integer memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profileImage;

    @Column(nullable = false)
    private Integer rewardPoint;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String socialType;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private Boolean activated;

    @Column(nullable = false)
    private Integer winRate;

    @OneToMany(mappedBy = "member")
    private Set<StudyMember> memberStudyMembers = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<AlarmRecipient> memberAlarmRecipients = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<StudyApplicant> memberStudyApplicants = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<OwnItem> memberOwnItems = new HashSet<>();

}
