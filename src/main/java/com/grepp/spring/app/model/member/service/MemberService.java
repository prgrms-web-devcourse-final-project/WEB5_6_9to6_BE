package com.grepp.spring.app.model.member.service;

import com.grepp.spring.app.controller.api.member.payload.request.MemberUpdateRequest;
import com.grepp.spring.app.model.auth.code.Role;
import com.grepp.spring.app.model.auth.dto.SignupRequest;
import com.grepp.spring.app.model.auth.dto.SocialMemberInfoRegistRequest;
import com.grepp.spring.app.model.member.code.SocialType;
import com.grepp.spring.app.model.member.dto.StudySummaryDto;
import com.grepp.spring.app.model.member.dto.response.MemberInfoResponse;
import com.grepp.spring.app.model.member.dto.response.MemberStudyListResponse;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.study.dto.ScheduleDto;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.infra.error.exceptions.AlreadyExistException;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import com.grepp.spring.infra.response.ResponseCode;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(SignupRequest req) {
        if (memberRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new AlreadyExistException(ResponseCode.ALREADY_EXIST);
        }

        Member member = Member.builder()
            .email(req.getEmail())
            .password(passwordEncoder.encode(req.getPassword()))
            .nickname(req.getNickname())
            .rewardPoints(100)
            .role(Role.ROLE_USER)
            .birthday(req.getBirthday())
            .gender(req.getGender())
            .socialType(SocialType.LOCAL)
            .winRate(0)
            .avatarImage(null)
            .build();

        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicatedEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void updateMemberInfoById(long memberId , SocialMemberInfoRegistRequest req) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND.message()));

        member.updateSocialInfo(
            req.getNickname(),
            req.getBirthday(),
            req.getGender()
        );
    }

    // 개인 정보 조회(이메일, 닉네임, 아바타)
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        return MemberInfoResponse.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .avatarImage(member.getAvatarImage())
            .build();
    }

    // 개인 정보 수정
    @Transactional
    public void updateMemberInfo(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 회원을 찾을 수 없습니다"));

        // 닉네임 변경
        if (request.getNickname() != null && !request.getNickname().isBlank()) {
            member.updateNickname(request.getNickname());
        }

        // 비밀번호 변경
        if (request.getCurrentPassword() != null && request.getNewPassword() != null && request.getNewPasswordCheck() != null)  {

            // 기존 비밀번호 일치 확인
            if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
            }

            // 새 비밀번호 확인
            if (!request.getNewPassword().equals(request.getNewPasswordCheck())) {
                throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
            }

            member.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        }
    }

    // 기존 비밀번호 일치 여부
    @Transactional(readOnly = true)
    public boolean verifyPassword(Long memberId, String inputPassword) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 회원을 찾을 수 없습니다"));

        return passwordEncoder.matches(inputPassword, member.getPassword());
    }

    // 사용자가 가입한 스터디 리스트 조회
    @Transactional(readOnly = true)
    public MemberStudyListResponse getMemberStudyList(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        List<StudySummaryDto> studyList = studyMemberRepository.findByMemberId(memberId)
            .stream()
            .map(sm -> {
                Study study = sm.getStudy();

                List<ScheduleDto> schedules = study.getSchedules().stream()
                    .map(s -> ScheduleDto.builder()
                        .dayOfWeek(s.getDayOfWeek().name())
                        .startTime(s.getStartTime())
                        .endTime(s.getEndTime())
                        .build())
                    .toList();

                return StudySummaryDto.builder()
                    .studyId(study.getStudyId())
                    .title(study.getName())
                    .currentMemberCount(study.getStudyMembers().size())
                    .maxMemberCount(study.getMaxMembers())
                    .category(study.getCategory().name())
                    .region(study.getRegion().name())
                    .place(study.getPlace())
                    .startDate(study.getStartDate().toString())
                    .endDate(study.getEndDate().toString())
                    .scheduleList(schedules)
                    .studyType(study.getStudyType().name())
                    .build();
            })
            .toList();

        return MemberStudyListResponse.builder()
            .memberId(member.getId())
            .nickname(member.getNickname())
            .studies(studyList)
            .build();
    }

}
