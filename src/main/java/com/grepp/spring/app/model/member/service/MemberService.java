package com.grepp.spring.app.model.member.service;

import com.grepp.spring.app.controller.api.member.payload.request.MemberUpdateRequest;
import com.grepp.spring.app.model.auth.code.Role;
import com.grepp.spring.app.model.auth.dto.SignupRequest;
import com.grepp.spring.app.model.auth.dto.SocialMemberInfoRegistRequest;
import com.grepp.spring.app.model.member.code.SocialType;
import com.grepp.spring.app.model.member.dto.StudySummaryDto;
import com.grepp.spring.app.model.member.dto.response.MemberInfoResponse;
import com.grepp.spring.app.model.member.dto.response.MemberStudyListResponse;
import com.grepp.spring.app.model.member.entity.Attendance;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.app.model.timer.repository.TimerRepository;
import com.grepp.spring.app.model.member.repository.StudyAttendanceRepository;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.study.dto.ScheduleDto;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.infra.error.exceptions.AlreadyCheckedAttendanceException;
import com.grepp.spring.infra.error.exceptions.AlreadyExistException;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import com.grepp.spring.infra.response.ResponseCode;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final TimerRepository timerRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudyAttendanceRepository studyAttendanceRepository;

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


    public Long findStudyMemberId(String email, Long studyId) {
        // email → member
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("해당 이메일의 회원이 존재하지 않습니다: " + email));

        // memberId → studyMember
        StudyMember studyMember = studyMemberRepository.findByMember_IdAndStudy_StudyId(member.getId(), studyId)
            .orElseThrow(() -> new RuntimeException("스터디 멤버를 찾을 수 없습니다."));

        return studyMember.getStudyMemberId();
    }

    public void markAttendance(Long studyMemberId) {
        StudyMember studyMember = studyMemberRepository.findById(studyMemberId)
            .orElseThrow(() -> new RuntimeException("스터디 멤버를 찾을 수 없습니다."));

        LocalDate today = LocalDate.now();

        // 이미 오늘 출석했는지 확인
        boolean alreadyAttended = studyAttendanceRepository
            .findByStudyMemberAndAttendanceDate(studyMember, today)
            .isPresent();

        if (alreadyAttended) {
            throw new AlreadyCheckedAttendanceException("이미 오늘 출석했습니다.");
        }

        // 출석 등록
        Attendance attendance = Attendance.builder()
            .studyMember(studyMember)
            .attendanceDate(today)
            .activated(true)
            .isAttended(true)
            .build();

        studyAttendanceRepository.save(attendance);
    }
}
