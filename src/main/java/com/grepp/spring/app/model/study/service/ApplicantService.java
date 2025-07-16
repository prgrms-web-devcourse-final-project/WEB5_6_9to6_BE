package com.grepp.spring.app.model.study.service;

import com.grepp.spring.app.model.study.code.ApplicantState;
import com.grepp.spring.app.model.study.repository.ApplicantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;


    @Transactional
    public void updateState(long memberId, long studyId, ApplicantState state) {
        applicantRepository.updateStateById(memberId, studyId, state);
    }

}
