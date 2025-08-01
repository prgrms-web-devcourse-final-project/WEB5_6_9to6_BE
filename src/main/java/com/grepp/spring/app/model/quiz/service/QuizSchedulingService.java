package com.grepp.spring.app.model.quiz.service;

import com.grepp.spring.app.model.quiz.amqp.QuizMessageProducer;
import com.grepp.spring.app.model.study.code.DayOfWeek;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import com.grepp.spring.app.model.study.repository.StudyScheduleRepository.StudyScheduleRepository;
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

    private final QuizMessageProducer quizMessageProducer;
    private final StudyScheduleRepository studyScheduleRepository;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    // 10분 후 시작하는 서바이벌 스터디 존재 시 퀴즈 생성 요청
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    public void createQuizBeforeStudyStarts() {

        LocalDateTime tenMinutesFromNow = LocalDateTime.now().plusMinutes(10);

        java.time.DayOfWeek javaDayOfWeek = tenMinutesFromNow.getDayOfWeek();

        String shortDayName = javaDayOfWeek.name().substring(0, 3);
        DayOfWeek targetDay = DayOfWeek.valueOf(shortDayName);

        String targetStartTimeString = tenMinutesFromNow.format(TIME_FORMATTER);
        List<StudySchedule> schedulesToRun = studyScheduleRepository.findSurvivalSchedules(targetDay, targetStartTimeString);

        if (schedulesToRun.isEmpty()) {
            return;
        }

        log.info("{} {}에 시작하는 {}개의 스터디에 대한 퀴즈 생성을 시작합니다.", targetDay, targetStartTimeString, schedulesToRun.size());

        for (StudySchedule schedule : schedulesToRun) {
            try {
                Long studyId = schedule.getStudy().getStudyId();

                quizMessageProducer.sendCreateNextQuizRequest(studyId);

                log.info("스터디 ID {}: 퀴즈 생성 요청을 RabbitMQ에 성공적으로 발행했습니다.", studyId);
            } catch (Exception e) {
                log.error("스터디 ID {}: 퀴즈 생성 요청 발행 중 오류 발생 - {}",
                        schedule.getStudy().getStudyId(), e.getMessage());
            }
        }
    }
}