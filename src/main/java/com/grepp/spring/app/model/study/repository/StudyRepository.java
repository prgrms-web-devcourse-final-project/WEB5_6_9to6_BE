package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.study.entity.Study;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {

    @Query("SELECT new com.grepp.spring.app.model.member.dto.response."
        + "ApplicantsResponse(a.id, a.member.id, a.member.nickname,"
        + " a.state, a.introduction) " +
        "FROM Applicant a " +
        "JOIN a.member " +
        "WHERE a.study.id = :studyId")
    List<ApplicantsResponse> findAllApplicants(@Param("studyId") Long studyId);
}
