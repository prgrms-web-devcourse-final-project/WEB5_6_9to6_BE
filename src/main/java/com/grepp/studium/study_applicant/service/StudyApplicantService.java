package com.grepp.studium.study_applicant.service;

import com.grepp.studium.member.domain.Member;
import com.grepp.studium.member.repos.MemberRepository;
import com.grepp.studium.study.domain.Study;
import com.grepp.studium.study.repos.StudyRepository;
import com.grepp.studium.study_applicant.domain.StudyApplicant;
import com.grepp.studium.study_applicant.model.StudyApplicantDTO;
import com.grepp.studium.study_applicant.repos.StudyApplicantRepository;
import com.grepp.studium.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StudyApplicantService {

    private final StudyApplicantRepository studyApplicantRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;

    public StudyApplicantService(final StudyApplicantRepository studyApplicantRepository,
            final MemberRepository memberRepository, final StudyRepository studyRepository) {
        this.studyApplicantRepository = studyApplicantRepository;
        this.memberRepository = memberRepository;
        this.studyRepository = studyRepository;
    }

    public List<StudyApplicantDTO> findAll() {
        final List<StudyApplicant> studyApplicants = studyApplicantRepository.findAll(Sort.by("applicationId"));
        return studyApplicants.stream()
                .map(studyApplicant -> mapToDTO(studyApplicant, new StudyApplicantDTO()))
                .toList();
    }

    public StudyApplicantDTO get(final Integer applicationId) {
        return studyApplicantRepository.findById(applicationId)
                .map(studyApplicant -> mapToDTO(studyApplicant, new StudyApplicantDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StudyApplicantDTO studyApplicantDTO) {
        final StudyApplicant studyApplicant = new StudyApplicant();
        mapToEntity(studyApplicantDTO, studyApplicant);
        return studyApplicantRepository.save(studyApplicant).getApplicationId();
    }

    public void update(final Integer applicationId, final StudyApplicantDTO studyApplicantDTO) {
        final StudyApplicant studyApplicant = studyApplicantRepository.findById(applicationId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studyApplicantDTO, studyApplicant);
        studyApplicantRepository.save(studyApplicant);
    }

    public void delete(final Integer applicationId) {
        studyApplicantRepository.deleteById(applicationId);
    }

    private StudyApplicantDTO mapToDTO(final StudyApplicant studyApplicant,
            final StudyApplicantDTO studyApplicantDTO) {
        studyApplicantDTO.setApplicationId(studyApplicant.getApplicationId());
        studyApplicantDTO.setState(studyApplicant.getState());
        studyApplicantDTO.setIntroduction(studyApplicant.getIntroduction());
        studyApplicantDTO.setActivated(studyApplicant.getActivated());
        studyApplicantDTO.setMember(studyApplicant.getMember() == null ? null : studyApplicant.getMember().getMemberId());
        studyApplicantDTO.setStudy(studyApplicant.getStudy() == null ? null : studyApplicant.getStudy().getStudyId());
        return studyApplicantDTO;
    }

    private StudyApplicant mapToEntity(final StudyApplicantDTO studyApplicantDTO,
            final StudyApplicant studyApplicant) {
        studyApplicant.setState(studyApplicantDTO.getState());
        studyApplicant.setIntroduction(studyApplicantDTO.getIntroduction());
        studyApplicant.setActivated(studyApplicantDTO.getActivated());
        final Member member = studyApplicantDTO.getMember() == null ? null : memberRepository.findById(studyApplicantDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        studyApplicant.setMember(member);
        final Study study = studyApplicantDTO.getStudy() == null ? null : studyRepository.findById(studyApplicantDTO.getStudy())
                .orElseThrow(() -> new NotFoundException("study not found"));
        studyApplicant.setStudy(study);
        return studyApplicant;
    }

}
