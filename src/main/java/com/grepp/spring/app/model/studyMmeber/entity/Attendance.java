package com.grepp.spring.app.model.studyMmeber.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "is_attended", nullable = false)
    private boolean isAttended;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_member_id", nullable = false)
    private StudyMember studyMember;

    @Column(nullable = false)
    private boolean activated;


    @Builder
    public Attendance(LocalDate attendanceDate, boolean isAttended, StudyMember studyMember, boolean activated) {
        this.attendanceDate = attendanceDate;
        this.isAttended = isAttended;
        this.studyMember = studyMember;
        this.activated = activated;
    }

    // 출석 체크
    public void markAsAttended() {
        this.isAttended = true;
    }

    // 비활성화 처리
    public void deactivate() {
        this.activated = false;
    }
}
