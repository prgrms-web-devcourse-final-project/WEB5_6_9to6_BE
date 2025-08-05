package com.grepp.spring.app.model.applicant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.applicant.code.ApplicantState;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.infra.entity.BaseEntity;
import com.grepp.spring.infra.error.exceptions.AlreadyProcessedException;
import com.grepp.spring.infra.error.exceptions.SameStateException;
import com.grepp.spring.infra.response.ResponseCode;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    @JsonIgnore
    private Study study;

    @Builder
    public Applicant(Long id, ApplicantState state, String introduction, Study study, Member member) {
        this.id = id;
        this.state = state;
        this.introduction = introduction;
        this.study = study;
        this.member = member;
    }

    public void changeState(ApplicantState newState) {
        // 현재 상태와 동일한 상태로 변경 요청
        if (this.state == newState) {
            throw new SameStateException(ResponseCode.BAD_REQUEST);
        }

        // 이미 처리된(승인/거절) 신청 건을 다시 변경
        if (this.state == ApplicantState.ACCEPT || this.state == ApplicantState.REJECT) {
            throw new AlreadyProcessedException(ResponseCode.BAD_REQUEST);
        }

        this.state = newState;
    }
}
