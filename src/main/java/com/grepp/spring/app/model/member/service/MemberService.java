package com.grepp.spring.app.model.member.service;

import com.grepp.spring.app.controller.api.member.payload.request.MemberUpdateRequest;
import com.grepp.spring.app.model.auth.code.Role;
import com.grepp.spring.app.model.auth.dto.SignupRequest;
import com.grepp.spring.app.model.auth.dto.SocialMemberInfoRegistRequest;
import com.grepp.spring.app.model.member.code.SocialType;
import com.grepp.spring.app.model.member.dto.response.AchievementRecordResponse;
import com.grepp.spring.app.model.member.dto.response.AvatarInfoResponse;
import com.grepp.spring.app.model.member.dto.response.MemberInfoResponse;
import com.grepp.spring.app.model.member.dto.response.MemberMyPageResponse;
import com.grepp.spring.app.model.member.dto.response.MemberStudyListResponse;
import com.grepp.spring.app.model.member.dto.response.MypageStudyInfoResponse;
import com.grepp.spring.app.model.member.dto.response.RequiredMemberInfoResponse;
import com.grepp.spring.app.model.member.dto.response.StudyInfoResponse;
import com.grepp.spring.app.model.member.entity.Attendance;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.app.model.member.repository.StudyAttendanceRepository;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.reward.repository.OwnItemIdGetRepository;
import com.grepp.spring.app.model.study.entity.GoalAchievement;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import com.grepp.spring.app.model.study.repository.GoalAchievementRepository;
import com.grepp.spring.infra.error.exceptions.AlreadyCheckedAttendanceException;
import com.grepp.spring.infra.error.exceptions.AlreadyExistException;
import com.grepp.spring.infra.error.exceptions.BadRequestException;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import com.grepp.spring.infra.response.ResponseCode;
import java.time.DayOfWeek;
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
    private final PasswordEncoder passwordEncoder;
    private final StudyAttendanceRepository studyAttendanceRepository;
    private final GoalAchievementRepository goalAchievementRepository;
    private final OwnItemIdGetRepository ownItemIdGetRepository;

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
            .winCount(0)
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
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        member.updateSocialInfo(
            req.getNickname(),
            req.getBirthday(),
            req.getGender()
        );
    }

    // 개인 정보 조회(이메일, 닉네임, 아바타)
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

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
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));

        // 닉네임 변경
        if (request.getNickname() != null && !request.getNickname().isBlank()) {
            member.updateNickname(request.getNickname());
        }

        // 비밀번호 변경
        if (request.getCurrentPassword() != null && request.getNewPassword() != null && request.getNewPasswordCheck() != null)  {

            // 기존 비밀번호 일치 확인
            if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
                throw new BadRequestException(ResponseCode.INCORRECT_PASSWORD);
            }

            // 새 비밀번호 확인
            if (!request.getNewPassword().equals(request.getNewPasswordCheck())) {
                throw new BadRequestException(ResponseCode.MISMATCH_PASSWORD_CONFIRM);
            }

            member.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        }
    }

    // 기존 비밀번호 일치 여부
    @Transactional(readOnly = true)
    public boolean verifyPassword(Long memberId, String inputPassword) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));

        return passwordEncoder.matches(inputPassword, member.getPassword());
    }

    // 사용자가 가입한 스터디 리스트 조회
    @Transactional(readOnly = true)
    public MemberStudyListResponse getMemberStudyList(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        List<StudyInfoResponse> studyList = studyMemberRepository.findByMemberId(memberId)
            .stream()
            .map(sm -> {
                Study study = sm.getStudy();

                List<StudySchedule> scheduleList = study.getSchedules();

                List<String> scheduleDays = scheduleList.stream()
                    .map(s -> s.getDayOfWeek().name())
                    .distinct()
                    .toList();

                String startTime = scheduleList.isEmpty() ? null : scheduleList.get(0).getStartTime();
                String endTime = scheduleList.isEmpty() ? null : scheduleList.get(0).getEndTime();

                return StudyInfoResponse.builder()
                    .studyId(study.getStudyId())
                    .title(study.getName())
                    .currentMemberCount(study.getStudyMembers().size())
                    .maxMemberCount(study.getMaxMembers())
                    .category(study.getCategory().name())
                    .region(study.getRegion().name())
                    .place(study.getPlace())
                    .startDate(study.getStartDate().toString())
                    .endDate(study.getEndDate().toString())
                    .schedules(scheduleDays)
                    .startTime(startTime)
                    .endTime(endTime)
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

    // 마이페이지 조회 (유저 정보 요청: 닉네임, 가입한 스터디 수, 우승 횟수, 포인트, 가입한 스터디 정보, 목표 달성)
    @Transactional(readOnly = true)
    public MemberMyPageResponse getMyPage(Long memberId) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        List<StudyMember> studyMembers = studyMemberRepository.findByMemberId(memberId);

        List<MypageStudyInfoResponse> userStudies = studyMembers.stream()
            .map(sm -> {
                Study study = sm.getStudy();

                List<StudySchedule> scheduleList = study.getSchedules();

                List<String> scheduleDays = scheduleList.stream()
                    .map(s -> s.getDayOfWeek().name())
                    .distinct()
                    .toList();

                String startTime = scheduleList.isEmpty() ? null : scheduleList.get(0).getStartTime();
                String endTime = scheduleList.isEmpty() ? null : scheduleList.get(0).getEndTime();

                List<GoalAchievement> goalAchievements = goalAchievementRepository
                    .findAllByStudyMemberAndIsAccomplishedTrue(sm);

                List<AchievementRecordResponse> achievementRecords = goalAchievements.stream()
                    .map(a -> AchievementRecordResponse.builder()
                        .isAccomplished(true)
                        .achievedAt(a.getAchievedAt().toString())
                        .build())
                    .toList();

                return MypageStudyInfoResponse.builder()
                    .title(study.getName())
                    .currentMemberCount(study.getStudyMembers().size())
                    .maxMemberCount(study.getMaxMembers())
                    .category(study.getCategory().name())
                    .region(study.getRegion().name())
                    .place(study.getPlace())
                    .schedules(scheduleDays)
                    .startTime(startTime)
                    .endTime(endTime)
                    .studyType(study.getStudyType().name())
                    .achievementRecords(achievementRecords)
                    .build();
            })
            .toList();

        return MemberMyPageResponse.builder()
            .nickname(member.getNickname())
            .joinedStudyCount(userStudies.size())
            .rewardPoints(member.getRewardPoints())
            .winCount(member.getWinCount())
            .userStudies(userStudies)
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

    // 출석 체크 등록
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

    // 주간 출석체크 조회
    public List<Attendance> getWeeklyAttendanceEntities(Long studyMemberId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        return studyAttendanceRepository.findByStudyMember_StudyMemberIdAndAttendanceDateBetween(
            studyMemberId,
            startOfWeek,
            endOfWeek
        );
    }

    public void updateProfileImage(Long memberId, String image) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException("Member not found"));
        member.updateAvatarImage(image);
    }

    @Transactional(readOnly = true)
    public RequiredMemberInfoResponse getMemberRequiredInfo(Long memberId) {
        return memberRepository.findRequiredMemberInfo(memberId);
    }

    @Transactional(readOnly = true)
    public AvatarInfoResponse getMemberAvatarInfo(Long memberId) {
        List<Long> ids = ownItemIdGetRepository.findOwnItemIdsByMemberId(memberId);
        String memberAvatarImage = memberRepository.findAvatarImageById(memberId);
        AvatarInfoResponse avatarInfoResponse = AvatarInfoResponse.builder()
            .itemIds(ids)
            .avatarImage(memberAvatarImage)
            .build();
        return avatarInfoResponse;
    }

    @Transactional
    public void addRewardPoints(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        member.addRewardPoints(100);
    }
}
