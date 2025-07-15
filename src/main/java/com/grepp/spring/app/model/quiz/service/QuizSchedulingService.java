package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.model.study.code.DayOfWeek;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import com.grepp.spring.app.model.study.repository.StudyScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizSchedulingService {

    private final QuizService quizService;
    private final StudyScheduleRepository studyScheduleRepository;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Scheduled(cron = "0 * * * * *")
    public void createQuizBeforeStudyStarts() {

        LocalDateTime tenMinutesFromNow = LocalDateTime.now().plusMinutes(10);

        java.time.DayOfWeek javaDayOfWeek = tenMinutesFromNow.getDayOfWeek();

        String shortDayName = javaDayOfWeek.name().substring(0, 3);
        DayOfWeek targetDay = DayOfWeek.valueOf(shortDayName);

        String targetStartTimeString = tenMinutesFromNow.format(TIME_FORMATTER);
        List<StudySchedule> schedulesToRun = studyScheduleRepository.findWithStudyByDayOfWeekAndStartTime(targetDay, targetStartTimeString);

        if (schedulesToRun.isEmpty()) {
            return;
        }

        log.info("{} {}에 시작하는 {}개의 스터디에 대한 퀴즈 생성을 시작합니다.", targetDay, targetStartTimeString, schedulesToRun.size());

        for (StudySchedule schedule : schedulesToRun) {
            try {
                Long studyId = schedule.getStudy().getStudyId();
                quizService.createNextQuizForStudy(studyId);
                log.info("스터디 ID {}: 시작 10분 전 퀴즈가 성공적으로 생성되었습니다.", studyId);
            } catch (Exception e) {
                log.error("스터디 ID {}: 예약된 퀴즈 생성 중 오류 발생 - {}",
                        schedule.getStudy().getStudyId(), e.getMessage());
            }
        }
    }
}