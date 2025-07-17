package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.study.code.StudyType;
import com.grepp.spring.app.model.study.entity.Study;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long>, StudyRepositoryCustom {

    // 지원자 목록 조회
    @Query("SELECT new com.grepp.spring.app.model.member.dto.response."
        + "ApplicantsResponse(a.id, a.member.id, a.member.nickname,"
        + " a.state, a.introduction) " +
        "FROM Applicant a " +
        "JOIN a.member " +
        "WHERE a.study.studyId = :studyId")
    List<ApplicantsResponse> findAllApplicants(@Param("studyId") Long studyId);

    // goals 만 fetch join
    @Query("SELECT s FROM Study s LEFT JOIN FETCH s.goals WHERE s.studyId = :id")
    Optional<Study> findByIdWithGoals(@Param("id") Long id);

    // schedules 만 fetch join
    @Query("SELECT s FROM Study s LEFT JOIN FETCH s.schedules WHERE s.studyId = :id")
    Optional<Study> findByIdWithSchedules(@Param("id") Long id);

    @Query("select s.studyType from Study s where s.studyId = :studyId")
    StudyType findStudyTypeById(Long studyId);

    @Query("select s.notice from Study s where s.studyId = :studyId")
    Optional<String> findNoticeByStudyId(Long studyId);
}

