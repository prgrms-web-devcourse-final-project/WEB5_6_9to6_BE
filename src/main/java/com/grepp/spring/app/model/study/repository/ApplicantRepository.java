package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.study.entity.Applicant;
import com.grepp.spring.app.model.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    boolean existsByStudyAndMember(Study study, Member member);
}

