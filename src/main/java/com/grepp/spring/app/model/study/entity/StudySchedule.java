package com.grepp.spring.app.model.study.entity;

import com.grepp.spring.app.model.study.code.DayOfWeek;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "study_schedule")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StudySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studyScheduleId;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private String startTime;

    private String endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;
}
