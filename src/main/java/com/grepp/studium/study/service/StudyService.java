package com.grepp.studium.study.service;

import com.grepp.studium.study.domain.Study;
import com.grepp.studium.study.model.StudyDTO;
import com.grepp.studium.study.repos.StudyRepository;
import com.grepp.studium.study_applicant.domain.StudyApplicant;
import com.grepp.studium.study_applicant.repos.StudyApplicantRepository;
import com.grepp.studium.study_goal.domain.StudyGoal;
import com.grepp.studium.study_goal.repos.StudyGoalRepository;
import com.grepp.studium.study_member.domain.StudyMember;
import com.grepp.studium.study_member.repos.StudyMemberRepository;
import com.grepp.studium.study_notice.domain.StudyNotice;
import com.grepp.studium.study_notice.repos.StudyNoticeRepository;
import com.grepp.studium.study_schedule.domain.StudySchedule;
import com.grepp.studium.study_schedule.repos.StudyScheduleRepository;
import com.grepp.studium.util.NotFoundException;
import com.grepp.studium.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyGoalRepository studyGoalRepository;
    private final StudyNoticeRepository studyNoticeRepository;
    private final StudyApplicantRepository studyApplicantRepository;
    private final StudyScheduleRepository studyScheduleRepository;

    public StudyService(final StudyRepository studyRepository,
            final StudyMemberRepository studyMemberRepository,
            final StudyGoalRepository studyGoalRepository,
            final StudyNoticeRepository studyNoticeRepository,
            final StudyApplicantRepository studyApplicantRepository,
            final StudyScheduleRepository studyScheduleRepository) {
        this.studyRepository = studyRepository;
        this.studyMemberRepository = studyMemberRepository;
        this.studyGoalRepository = studyGoalRepository;
        this.studyNoticeRepository = studyNoticeRepository;
        this.studyApplicantRepository = studyApplicantRepository;
        this.studyScheduleRepository = studyScheduleRepository;
    }

    public List<StudyDTO> findAll() {
        final List<Study> studies = studyRepository.findAll(Sort.by("studyId"));
        return studies.stream()
                .map(study -> mapToDTO(study, new StudyDTO()))
                .toList();
    }

    public StudyDTO get(final Integer studyId) {
        return studyRepository.findById(studyId)
                .map(study -> mapToDTO(study, new StudyDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StudyDTO studyDTO) {
        final Study study = new Study();
        mapToEntity(studyDTO, study);
        return studyRepository.save(study).getStudyId();
    }

    public void update(final Integer studyId, final StudyDTO studyDTO) {
        final Study study = studyRepository.findById(studyId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studyDTO, study);
        studyRepository.save(study);
    }

    public void delete(final Integer studyId) {
        studyRepository.deleteById(studyId);
    }

    private StudyDTO mapToDTO(final Study study, final StudyDTO studyDTO) {
        studyDTO.setStudyId(study.getStudyId());
        studyDTO.setName(study.getName());
        studyDTO.setCategory(study.getCategory());
        studyDTO.setMaxMembers(study.getMaxMembers());
        studyDTO.setRegion(study.getRegion());
        studyDTO.setPlace(study.getPlace());
        studyDTO.setIsOnline(study.getIsOnline());
        studyDTO.setStartDate(study.getStartDate());
        studyDTO.setEndDate(study.getEndDate());
        studyDTO.setCreatedAt(study.getCreatedAt());
        studyDTO.setStatus(study.getStatus());
        studyDTO.setDescription(study.getDescription());
        studyDTO.setExternalLink(study.getExternalLink());
        studyDTO.setStudyType(study.getStudyType());
        studyDTO.setActivated(study.getActivated());
        return studyDTO;
    }

    private Study mapToEntity(final StudyDTO studyDTO, final Study study) {
        study.setName(studyDTO.getName());
        study.setCategory(studyDTO.getCategory());
        study.setMaxMembers(studyDTO.getMaxMembers());
        study.setRegion(studyDTO.getRegion());
        study.setPlace(studyDTO.getPlace());
        study.setIsOnline(studyDTO.getIsOnline());
        study.setStartDate(studyDTO.getStartDate());
        study.setEndDate(studyDTO.getEndDate());
        study.setCreatedAt(studyDTO.getCreatedAt());
        study.setStatus(studyDTO.getStatus());
        study.setDescription(studyDTO.getDescription());
        study.setExternalLink(studyDTO.getExternalLink());
        study.setStudyType(studyDTO.getStudyType());
        study.setActivated(studyDTO.getActivated());
        return study;
    }

    public ReferencedWarning getReferencedWarning(final Integer studyId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Study study = studyRepository.findById(studyId)
                .orElseThrow(NotFoundException::new);
        final StudyMember studyStudyMember = studyMemberRepository.findFirstByStudy(study);
        if (studyStudyMember != null) {
            referencedWarning.setKey("study.studyMember.study.referenced");
            referencedWarning.addParam(studyStudyMember.getStudyMemberId());
            return referencedWarning;
        }
        final StudyGoal studyStudyGoal = studyGoalRepository.findFirstByStudy(study);
        if (studyStudyGoal != null) {
            referencedWarning.setKey("study.studyGoal.study.referenced");
            referencedWarning.addParam(studyStudyGoal.getGoalId());
            return referencedWarning;
        }
        final StudyNotice studyStudyNotice = studyNoticeRepository.findFirstByStudy(study);
        if (studyStudyNotice != null) {
            referencedWarning.setKey("study.studyNotice.study.referenced");
            referencedWarning.addParam(studyStudyNotice.getNoticeId());
            return referencedWarning;
        }
        final StudyApplicant studyStudyApplicant = studyApplicantRepository.findFirstByStudy(study);
        if (studyStudyApplicant != null) {
            referencedWarning.setKey("study.studyApplicant.study.referenced");
            referencedWarning.addParam(studyStudyApplicant.getApplicationId());
            return referencedWarning;
        }
        final StudySchedule studyStudySchedule = studyScheduleRepository.findFirstByStudy(study);
        if (studyStudySchedule != null) {
            referencedWarning.setKey("study.studySchedule.study.referenced");
            referencedWarning.addParam(studyStudySchedule.getId());
            return referencedWarning;
        }
        return null;
    }

}
