package com.grepp.spring.app.model.study.service;

import com.grepp.spring.app.model.member.code.StudyRole;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.repository.StudyRepository;
import com.grepp.spring.infra.error.exceptions.AlreadyExistException;
import com.grepp.spring.infra.response.ResponseCode;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveMember(Long studyId, long memberId) {

        // 중복 등록 방지
        if (studyMemberRepository.existsByMember_IdAndStudy_StudyId(memberId, studyId)) {
            throw new AlreadyExistException(ResponseCode.ALREADY_EXIST);
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 회원을 찾을 수 없습니다: " + memberId));
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 스터디를 찾을 수 없습니다: " + studyId));

        StudyMember studyMember = StudyMember.builder()
            .member(member)
            .study(study)
            .studyRole(StudyRole.MEMBER)
            .activated(true)
            .build();

        studyMemberRepository.save(studyMember);

    }
}
