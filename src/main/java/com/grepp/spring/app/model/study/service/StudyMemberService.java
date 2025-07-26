package com.grepp.spring.app.model.study.service;

import com.grepp.spring.app.controller.api.study.payload.CheckGoalResponse;
import com.grepp.spring.app.model.member.code.StudyRole;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.repository.GoalAchievementRepository;
import com.grepp.spring.app.model.study.repository.StudyRepository;
import com.grepp.spring.infra.error.exceptions.AlreadyExistException;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import com.grepp.spring.infra.response.ResponseCode;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final GoalAchievementRepository goalAchievementRepository;

    @Transactional
    public void saveMember(Long studyId, Long memberId) {

        // 중복 등록 방지
        if (studyMemberRepository.existsByMember_IdAndStudy_StudyId(memberId, studyId)) {
            throw new AlreadyExistException(ResponseCode.ALREADY_EXIST);
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND.message()));
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND.message()));

        StudyMember studyMember = StudyMember.builder()
            .member(member)
            .study(study)
            .studyRole(StudyRole.MEMBER)
            .activated(true)
            .build();

        studyMemberRepository.save(studyMember);

    }

    @Transactional
    public void applyToStudy(Long memberId, Long studyId) {
        Study study = studyRepository.findById(studyId)
            .orElseThrow(() -> new NotFoundException("해당 스터디가 존재하지 않습니다."));

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("회원 정보를 찾을 수 없습니다."));

        // 중복 가입 방지
        if (studyMemberRepository.existStudyMember(memberId, studyId)) {
            throw new AlreadyExistException(ResponseCode.ALREADY_EXIST);
        }

        StudyMember newMember = StudyMember.builder()
            .study(study)
            .member(member)
             .studyRole(StudyRole.MEMBER)
            .build();
        studyMemberRepository.save(newMember);
    }

    @Transactional(readOnly = true)
    public List<CheckGoalResponse> getGoalStatuses(Long studyId, Long memberId) {
        Long studyMemberId = studyMemberRepository.findStudyMemberId(studyId, memberId)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND.message()));
        List<CheckGoalResponse> res = goalAchievementRepository
            .findAchieveStatuses(studyId, studyMemberId, LocalDateTime.now());
        return res;
    }

}
