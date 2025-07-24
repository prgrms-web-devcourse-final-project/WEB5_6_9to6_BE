package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.study.code.StudyType;
import com.grepp.spring.app.model.study.entity.Study;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudyRepositoryCustom {
    List<Study> searchStudiesPage(StudySearchRequest request);

    Page<Study> searchStudiesPage(StudySearchRequest req, Pageable pageable);

    Optional<Study> findByIdWithSchedulesAndGoals(Long studyId);

    List<ApplicantsResponse> findApplicants(Long studyId);

    // goals 만 fetch join
    Optional<Study> findWithGoals(Long id);

    // schedules 만 fetch join
    Optional<Study> findWithStudySchedules(Long id);

    StudyType findStudyType(Long studyId);

    Optional<String> findNotice(Long studyId);
}
