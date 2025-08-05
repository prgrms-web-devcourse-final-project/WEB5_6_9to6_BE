package com.grepp.spring.app.model.study.service;

import com.grepp.spring.app.model.studyMmeber.repository.StudyMemberRepository;
import com.grepp.spring.app.model.study.code.ApplicantState;
import com.grepp.spring.app.model.study.entity.Applicant;
import com.grepp.spring.app.model.study.repository.applicant.ApplicantRepository;
import com.grepp.spring.infra.error.exceptions.HasNotRightException;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import com.grepp.spring.infra.error.exceptions.NullStateException;
import com.grepp.spring.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final StudyMemberRepository studyMemberRepository;
    private final ApplicantRepository applicantRepository;


    @Transactional
    public void updateState(long acceptorId, long memberId, long studyId, ApplicantState state) {
        if (state == null) throw new NullStateException(ResponseCode.BAD_REQUEST);

        if(!studyMemberRepository.checkAcceptorHasRight(acceptorId, studyId)) {
            throw new HasNotRightException(ResponseCode.UNAUTHORIZED);
        }

        Applicant applicant = applicantRepository.findByMember_IdAndStudy_StudyId(memberId, studyId)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND.message()));

        applicantRepository.updateStateById(memberId, studyId, state);
        applicant.changeState(state);
    }

}
