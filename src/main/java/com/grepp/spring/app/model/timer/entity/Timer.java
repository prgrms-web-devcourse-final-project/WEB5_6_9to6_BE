package com.grepp.spring.app.model.timer.entity;

import com.grepp.spring.infra.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity @NoArgsConstructor
public class Timer extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long studyId;
    Long studyMemberId;

    Integer dailyStudyTime;

    @Builder
    public Timer(Long id, Integer dailyStudyTime) {
        this.id = id;
        this.dailyStudyTime = dailyStudyTime;
    }

}
