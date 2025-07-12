package com.grepp.spring.app.model.study.service;

import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.study.dto.StudyInfoResponse;
import com.grepp.spring.app.model.study.dto.StudyListResponse;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.repository.StudyRepository;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;

    //필터 조건에 따라 스터디 목록 + 현재 인원 수 조회


    // 필터 조건에 따라 스터디 목록 + 현재 인원 수 조회
    public List<StudyListResponse> searchStudiesWithMemberCount(StudySearchRequest req) {
        List<Study> studies = studyRepository.searchByFilterWithSchedules(req);

        return studies.stream()
            .map(study -> {
                int currentMemberCount = studyMemberRepository.countByStudy_StudyId(study.getStudyId());
                return StudyListResponse.fromEntity(study, currentMemberCount);
            })
            .collect(Collectors.toList());
    }

    // 스터디 지원자 목록 조회
    @Transactional(readOnly = true)
    public List<ApplicantsResponse> getApplicants(Long studyId) {
        return studyRepository.findAllApplicants(studyId);
    }

    @Transactional(readOnly = true)
    public StudyInfoResponse getStudyInfo(Long studyId) {
        Study studyWithGoals = studyRepository.findByIdWithGoals(studyId)
            .orElseThrow(() -> new NotFoundException("스터디가 존재하지 않습니다."));

        Study studyWithSchedules = studyRepository.findByIdWithSchedules(studyId)
            .orElseThrow(() -> new NotFoundException("스터디가 존재하지 않습니다."));

        // goals fetch join 과 schedules fetch join 을 합치기
        studyWithGoals.getSchedules().addAll(studyWithSchedules.getSchedules());

        return StudyInfoResponse.fromEntity(studyWithGoals);
    }

}
