package com.grepp.studium.study_schedule.domain;

import com.grepp.studium.study.domain.Study;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "StudySchedules")
@Getter
@Setter
public class StudySchedule {

    @Id
    @Column(nullable = false, updatable = false)
    private String id;

    @Column
    private String dayOfWeek;

    @Column
    private String startTime;

    @Column
    private String endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private Study study;

}
