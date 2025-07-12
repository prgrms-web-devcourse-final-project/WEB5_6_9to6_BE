package com.grepp.spring.app.model.member.entity;

import com.grepp.spring.infra.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Entity
@NoArgsConstructor
public class TimerSetting extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studyMemberId;

    private Integer focusTime;
    private Integer restTime;

    @Builder
    public TimerSetting(Long id, Long studyMemberId, Integer focusTime, Integer restTime) {
        this.id = id;
        this.studyMemberId = studyMemberId;
        this.focusTime = focusTime;
        this.restTime = restTime;
    }

}
