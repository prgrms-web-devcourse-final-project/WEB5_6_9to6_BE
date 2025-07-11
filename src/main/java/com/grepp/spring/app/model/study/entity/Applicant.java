package com.grepp.spring.app.model.study.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.study.code.ApplicantState;
import com.grepp.spring.infra.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Applicant extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ApplicantState state;

    private String introduction;

    private Long memberId;

    @ManyToOne @JsonIgnore
    @JoinColumn(name = "study_id")
    private Study study;

    @Builder
    public Applicant(Long id, ApplicantState state, String introduction, Long memberId,
        Study study) {
        this.id = id;
        this.state = state;
        this.introduction = introduction;
        this.memberId = memberId;
        this.study = study;
    }

    protected void setStudy(Study study) {
        this.study = study;
    }

}
