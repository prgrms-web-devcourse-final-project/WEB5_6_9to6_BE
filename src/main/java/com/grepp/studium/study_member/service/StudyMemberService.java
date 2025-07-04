package com.grepp.studium.study_member.service;

import com.grepp.studium.attendance.domain.Attendance;
import com.grepp.studium.attendance.repos.AttendanceRepository;
import com.grepp.studium.goal_achievement.domain.GoalAchievement;
import com.grepp.studium.goal_achievement.repos.GoalAchievementRepository;
import com.grepp.studium.member.domain.Member;
import com.grepp.studium.member.repos.MemberRepository;
import com.grepp.studium.quiz_record.domain.QuizRecord;
import com.grepp.studium.quiz_record.repos.QuizRecordRepository;
import com.grepp.studium.study.domain.Study;
import com.grepp.studium.study.repos.StudyRepository;
import com.grepp.studium.study_member.domain.StudyMember;
import com.grepp.studium.study_member.model.StudyMemberDTO;
import com.grepp.studium.study_member.repos.StudyMemberRepository;
import com.grepp.studium.timer.domain.Timer;
import com.grepp.studium.timer.repos.TimerRepository;
import com.grepp.studium.util.NotFoundException;
import com.grepp.studium.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final GoalAchievementRepository goalAchievementRepository;
    private final AttendanceRepository attendanceRepository;
    private final QuizRecordRepository quizRecordRepository;
    private final TimerRepository timerRepository;

    public StudyMemberService(final StudyMemberRepository studyMemberRepository,
            final MemberRepository memberRepository, final StudyRepository studyRepository,
            final GoalAchievementRepository goalAchievementRepository,
            final AttendanceRepository attendanceRepository,
            final QuizRecordRepository quizRecordRepository,
            final TimerRepository timerRepository) {
        this.studyMemberRepository = studyMemberRepository;
        this.memberRepository = memberRepository;
        this.studyRepository = studyRepository;
        this.goalAchievementRepository = goalAchievementRepository;
        this.attendanceRepository = attendanceRepository;
        this.quizRecordRepository = quizRecordRepository;
        this.timerRepository = timerRepository;
    }

    public List<StudyMemberDTO> findAll() {
        final List<StudyMember> studyMembers = studyMemberRepository.findAll(Sort.by("studyMemberId"));
        return studyMembers.stream()
                .map(studyMember -> mapToDTO(studyMember, new StudyMemberDTO()))
                .toList();
    }

    public StudyMemberDTO get(final Integer studyMemberId) {
        return studyMemberRepository.findById(studyMemberId)
                .map(studyMember -> mapToDTO(studyMember, new StudyMemberDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final StudyMemberDTO studyMemberDTO) {
        final StudyMember studyMember = new StudyMember();
        mapToEntity(studyMemberDTO, studyMember);
        return studyMemberRepository.save(studyMember).getStudyMemberId();
    }

    public void update(final Integer studyMemberId, final StudyMemberDTO studyMemberDTO) {
        final StudyMember studyMember = studyMemberRepository.findById(studyMemberId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studyMemberDTO, studyMember);
        studyMemberRepository.save(studyMember);
    }

    public void delete(final Integer studyMemberId) {
        studyMemberRepository.deleteById(studyMemberId);
    }

    private StudyMemberDTO mapToDTO(final StudyMember studyMember,
            final StudyMemberDTO studyMemberDTO) {
        studyMemberDTO.setStudyMemberId(studyMember.getStudyMemberId());
        studyMemberDTO.setActivated(studyMember.getActivated());
        studyMemberDTO.setRole(studyMember.getRole());
        studyMemberDTO.setMember(studyMember.getMember() == null ? null : studyMember.getMember().getMemberId());
        studyMemberDTO.setStudy(studyMember.getStudy() == null ? null : studyMember.getStudy().getStudyId());
        return studyMemberDTO;
    }

    private StudyMember mapToEntity(final StudyMemberDTO studyMemberDTO,
            final StudyMember studyMember) {
        studyMember.setActivated(studyMemberDTO.getActivated());
        studyMember.setRole(studyMemberDTO.getRole());
        final Member member = studyMemberDTO.getMember() == null ? null : memberRepository.findById(studyMemberDTO.getMember())
                .orElseThrow(() -> new NotFoundException("member not found"));
        studyMember.setMember(member);
        final Study study = studyMemberDTO.getStudy() == null ? null : studyRepository.findById(studyMemberDTO.getStudy())
                .orElseThrow(() -> new NotFoundException("study not found"));
        studyMember.setStudy(study);
        return studyMember;
    }

    public ReferencedWarning getReferencedWarning(final Integer studyMemberId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final StudyMember studyMember = studyMemberRepository.findById(studyMemberId)
                .orElseThrow(NotFoundException::new);
        final GoalAchievement studyMemberGoalAchievement = goalAchievementRepository.findFirstByStudyMember(studyMember);
        if (studyMemberGoalAchievement != null) {
            referencedWarning.setKey("studyMember.goalAchievement.studyMember.referenced");
            referencedWarning.addParam(studyMemberGoalAchievement.getAchievementId());
            return referencedWarning;
        }
        final Attendance studyMemberAttendance = attendanceRepository.findFirstByStudyMember(studyMember);
        if (studyMemberAttendance != null) {
            referencedWarning.setKey("studyMember.attendance.studyMember.referenced");
            referencedWarning.addParam(studyMemberAttendance.getAttendanceId());
            return referencedWarning;
        }
        final QuizRecord studyMemberQuizRecord = quizRecordRepository.findFirstByStudyMember(studyMember);
        if (studyMemberQuizRecord != null) {
            referencedWarning.setKey("studyMember.quizRecord.studyMember.referenced");
            referencedWarning.addParam(studyMemberQuizRecord.getQuizRecordId());
            return referencedWarning;
        }
        final Timer studyMemberTimer = timerRepository.findFirstByStudyMember(studyMember);
        if (studyMemberTimer != null) {
            referencedWarning.setKey("studyMember.timer.studyMember.referenced");
            referencedWarning.addParam(studyMemberTimer.getTimerId());
            return referencedWarning;
        }
        return null;
    }

}
