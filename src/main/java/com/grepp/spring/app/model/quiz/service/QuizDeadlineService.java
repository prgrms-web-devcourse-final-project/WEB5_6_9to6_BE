package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.model.studymember.entity.StudyMember;
import com.grepp.spring.app.model.studymember.repository.StudyMemberRepository;
import com.grepp.spring.app.model.quiz.entity.QuizRecord;
import com.grepp.spring.app.model.quiz.repository.QuizRecordRepository;
import com.grepp.spring.app.model.quiz.repository.quizSetRepository.QuizSetRepository;
import com.grepp.spring.app.model.study.code.DayOfWeek;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import com.grepp.spring.app.model.study.repository.studyScheduleRepository.StudyScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizDeadlineService {

    private final StudyScheduleRepository studyScheduleRepository;
    private final QuizSetRepository quizSetRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final QuizRecordRepository quizRecordRepository;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    @Transactional
    public void checkQuizSubmissionsAndDeactivateMembers() {

        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        String targetEndTime = oneMinuteAgo.format(TIME_FORMATTER);
        DayOfWeek targetDay = DayOfWeek.valueOf(oneMinuteAgo.getDayOfWeek().name().substring(0, 3));

        List<StudySchedule> endedSchedules = studyScheduleRepository.findWithStudyByDayOfWeekAndEndTime(targetDay, targetEndTime);

        if (endedSchedules.isEmpty()) {
            return;
        }

        log.info("{} {}에 마감된 {}개의 스터디에 대해 미제출자 탈락 처리를 시작합니다.", targetDay, targetEndTime, endedSchedules.size());

        for (StudySchedule schedule : endedSchedules) {
            Long studyId = schedule.getStudy().getStudyId();

            quizSetRepository.findTopByStudyIdOrderByWeekDesc(studyId).ifPresent(latestQuizSet -> {

                List<QuizRecord> submissions = quizRecordRepository.findAllByQuizSet(latestQuizSet);
                Set<Long> submittedMemberIds = submissions.stream()
                        .map(QuizRecord::getStudyMemberId)
                        .collect(Collectors.toSet());

                List<StudyMember> activeMembers = studyMemberRepository.findAllByStudy_StudyIdAndActivatedIsTrue(studyId);

                for (StudyMember member : activeMembers) {
                    if (!submittedMemberIds.contains(member.getStudyMemberId())) {
                        member.unActivated();
                        log.info("스터디 ID {}: 스터디 멤버 ID {}가 시간 내 퀴즈 미제출로 탈락 처리되었습니다.", studyId, member.getStudyMemberId());
                    }
                }
            });
        }
    }
}