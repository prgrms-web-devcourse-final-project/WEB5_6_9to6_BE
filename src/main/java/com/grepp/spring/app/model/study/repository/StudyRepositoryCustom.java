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
    List<Study> searchByFilterWithSchedules(StudySearchRequest request);

    Page<Study> searchByFilterWithSchedules(StudySearchRequest req, Pageable pageable);

    Optional<Study> findByIdWithSchedulesAndGoals(Long studyId);

    List<ApplicantsResponse> findAllApplicants(Long studyId);

    // goals 만 fetch join
    Optional<Study> findByIdWithGoals(Long id);

    // schedules 만 fetch join
    Optional<Study> findByIdWithSchedules(Long id);

    StudyType findStudyTypeById(Long studyId);

    Optional<String> findNoticeByStudyId(Long studyId);
}
