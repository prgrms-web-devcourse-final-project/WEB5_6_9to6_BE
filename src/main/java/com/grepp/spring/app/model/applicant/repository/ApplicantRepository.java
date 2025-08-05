package com.grepp.spring.app.model.applicant.repository;

import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.applicant.code.ApplicantState;
import com.grepp.spring.app.model.applicant.entity.Applicant;
import com.grepp.spring.app.model.study.entity.Study;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    boolean existsByStudyAndMember(Study study, Member member);

    @Modifying
    @Query("update Applicant a set a.state = :state "
        + "where a.member.id = :memberId and a.study.studyId = :studyId")
    void updateStateById(@Param("memberId") long memberId, @Param("studyId") long studyId, @Param("state") ApplicantState state);

    Optional<Applicant> findByMember_IdAndStudy_StudyId(Long memberId, Long studyId);

}

