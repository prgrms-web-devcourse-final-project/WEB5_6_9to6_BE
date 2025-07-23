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
import com.grepp.spring.app.model.reward.entity.OwnItem;
import com.grepp.spring.app.model.reward.entity.RewardItem;
import com.grepp.spring.app.model.reward.repository.ItemSetRepository;
import com.grepp.spring.app.model.reward.repository.OwnItemIdGetRepository;
import com.grepp.spring.app.model.reward.repository.OwnItemRepository;
import com.grepp.spring.app.model.reward.repository.RewardItemRepository;
import com.grepp.spring.app.model.study.entity.GoalAchievement;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import com.grepp.spring.app.model.study.repository.GoalAchievementRepository;
import com.grepp.spring.infra.error.exceptions.AlreadyCheckedAttendanceException;
import com.grepp.spring.infra.error.exceptions.AlreadyExistException;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import com.grepp.spring.infra.error.exceptions.PasswordValidationException;
import com.grepp.spring.infra.response.ResponseCode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    private final OwnItemRepository ownItemRepository;
    private final RewardItemRepository rewardItemRepository;
    private final ItemSetRepository itemSetRepository;

    @Transactional
    public Member join(SignupRequest req) {
        if (memberRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new AlreadyExistException(ResponseCode.ALREADY_EXIST);
        }

        // 1. 기본 아이템 rewardItem 중 item_id = 1인 것 가져오기
        RewardItem defaultThemeItem = getRewardItem(1L);

        // 2. 해당 item 의 image 가져오기
        String defaultAvatarImage = itemSetRepository.findImageByItemId(1L);


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
            .avatarImage(defaultAvatarImage)
            .build();

        // 1. 회원 저장
        Member savedMember = memberRepository.save(member);

        // 2. 기본 아이템 목록 정의
        List<OwnItem> defaultItems = List.of(
            OwnItem.builder().memberId(savedMember.getId()).rewardItem(getRewardItem(1L)).isUsed(true).activated(true).build(),
            OwnItem.builder().memberId(savedMember.getId()).rewardItem(getRewardItem(11L)).isUsed(true).activated(true).build(),
            OwnItem.builder().memberId(savedMember.getId()).rewardItem(getRewardItem(21L)).isUsed(true).activated(true).build(),
            OwnItem.builder().memberId(savedMember.getId()).rewardItem(getRewardItem(31L)).isUsed(true).activated(true).build(),
            OwnItem.builder().memberId(savedMember.getId()).rewardItem(getRewardItem(51L)).isUsed(false).activated(true).build(),
            OwnItem.builder().memberId(savedMember.getId()).rewardItem(getRewardItem(52L)).isUsed(true).activated(true).build(),
            OwnItem.builder().memberId(savedMember.getId()).rewardItem(getRewardItem(61L)).isUsed(true).activated(true).build(),
            OwnItem.builder().memberId(savedMember.getId()).rewardItem(getRewardItem(62L)).isUsed(false).activated(true).build()
        );

        ownItemRepository.saveAll(defaultItems);

        return savedMember;
    }

    private RewardItem getRewardItem(Long itemId) {
        return rewardItemRepository.findById(itemId)
            .orElseThrow(() -> new NotFoundException("해당 item_id를 찾을 수 없습니다: " + itemId));
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
        if (StringUtils.hasText(request.getNickname())) {
            member.updateNickname(request.getNickname());
        }

        // 비밀번호 변경
        if (StringUtils.hasText(request.getCurrentPassword()) &&
            StringUtils.hasText(request.getNewPassword())) {

            // 기존 비밀번호 일치 확인
            if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
                throw new PasswordValidationException(ResponseCode.INCORRECT_PASSWORD);
            }

            // 새 비밀번호가 기존 비밀번호와 같은지 검증 (같으면 예외처리)
            if (passwordEncoder.matches(request.getNewPassword(), member.getPassword())) {
                throw new PasswordValidationException(ResponseCode.SAME_PASSWORD_NOT_ALLOWED);
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

        List<StudyInfoResponse> studyList = studyMemberRepository.findActiveStudyMemberships(memberId)
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

        List<StudyMember> studyMembers = studyMemberRepository.findActiveStudyMemberships(memberId);

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

    public Long findMemberIdByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("해당 이메일의 회원이 존재하지 않습니다."));
        return member.getId();
    }

    public Long findStudyMemberId(String email, Long studyId) {
        // email → member
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("해당 이메일의 회원이 존재하지 않습니다: " + email));

        // memberId → studyMember
        StudyMember studyMember = studyMemberRepository.findActiveStudyMember(member.getId(), studyId)
            .orElseThrow(() -> new NotFoundException("활성화된 스터디 멤버를 찾을 수 없습니다."));

        return studyMember.getStudyMemberId();
    }

    public Long findStudyMemberId(Long memberId, Long studyId) {
        StudyMember studyMember = studyMemberRepository.findActiveStudyMember(memberId, studyId)
            .orElseThrow(() -> new NotFoundException("스터디 멤버를 찾을 수 없습니다."));

        return studyMember.getStudyMemberId();
    }

    // 출석 체크 등록
    public void markAttendance(Long studyMemberId) {
        StudyMember studyMember = studyMemberRepository.findById(studyMemberId)
            .orElseThrow(() -> new NotFoundException("스터디 멤버를 찾을 수 없습니다."));

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
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        member.updateAvatarImage(image);
    }

    @Transactional(readOnly = true)
    public RequiredMemberInfoResponse getMemberRequiredInfo(Long memberId) {
        return memberRepository.findRequiredMemberInfo(memberId);
    }

    @Transactional(readOnly = true)
    public AvatarInfoResponse getMemberAvatarInfo(Long memberId) {
        List<Long> ids = ownItemIdGetRepository.findIdsByMemberId(memberId);
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
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        member.addRewardPoints(100);
    }
}
