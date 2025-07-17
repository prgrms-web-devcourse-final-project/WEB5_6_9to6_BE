package com.grepp.spring.app.model.timer.service;

import com.grepp.spring.app.controller.api.timer.payload.StudyTimeRecordRequest;
import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.timer.dto.DailyStudyLogResponse;
import com.grepp.spring.app.model.timer.dto.StudyWeekTimeResponse;
import com.grepp.spring.app.model.timer.dto.TotalStudyTimeResponse;
import com.grepp.spring.app.model.timer.entity.Timer;
import com.grepp.spring.app.model.timer.repository.TimerQueryRepository;
import com.grepp.spring.app.model.timer.repository.TimerRepository;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.querydsl.core.Tuple;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimerService {

    private final TimerRepository timerRepository;
    private final TimerQueryRepository timerQueryRepository;
    private final StudyMemberRepository studyMemberRepository;

    @Transactional(readOnly = true)
    public TotalStudyTimeResponse getAllStudyTime(Long memberId) {
        List<Long> studyMemberIds = studyMemberRepository.findAllStudies(memberId);
        if (studyMemberIds == null || studyMemberIds.isEmpty()) {
            log.info("참여 중인 스터디가 없습니다. memberId: {}", memberId);
            return TotalStudyTimeResponse.builder()
                .totalStudyTime(0L)
                .build();
        }

        log.info("조회된 studyMemberIds: {}", studyMemberIds);
        Long totalStudyTime = timerRepository.findTotalStudyTimeByStudyMemberIds(studyMemberIds);

        return TotalStudyTimeResponse.builder()
            .totalStudyTime(totalStudyTime != null ? totalStudyTime : 0L)
            .build();
    }

    @Transactional
    public List<DailyStudyLogResponse> findDailyStudyLogsByStudyMemberId(Long studyId, Long memberId) {
        Long studyMemberId = studyMemberRepository.findIdByStudyMemberIdAndMemberId(studyId, memberId)
            .orElseThrow(() -> new NotFoundException("Study Member Not Found"));
        LocalDateTime endOfDay = LocalDateTime.now();
        LocalDateTime startOfDay = endOfDay.toLocalDate().minusDays(6).atStartOfDay();

        List<Tuple> results = timerQueryRepository.findDailyStudyLogsByStudyMemberId(studyMemberId, studyId, startOfDay, endOfDay);

        return results.stream()
            .map(tuple -> new DailyStudyLogResponse(
                tuple.get(0, Date.class).toLocalDate(),
                tuple.get(1, Long.class)
            ))
            .toList();
    }

    @Transactional(readOnly = true)
    public StudyWeekTimeResponse getStudyTimeForPeriod(Long studyId, Long memberId) {
        Long studyMemberId = studyMemberRepository.findIdByStudyMemberIdAndMemberId(studyId, memberId)
            .orElseThrow(() -> new NotFoundException("Study Member Not Found"));

        LocalDateTime endOfDay = LocalDateTime.now();
        LocalDateTime startOfDay = endOfDay.toLocalDate().minusDays(6).atStartOfDay();

        Long totalTime = timerQueryRepository.findTotalStudyTimeInPeriod(studyMemberId, studyId, startOfDay, endOfDay);

        return StudyWeekTimeResponse.builder()
            .totalStudyTime(totalTime != null ? totalTime : 0L)
            .build();
    }

    @Transactional
    public void recordStudyTime(Long studyId, Long memberId, StudyTimeRecordRequest req) {

        Long studyMemberId = studyMemberRepository.findStudyMemberIdByStudyIdWithMeberId(studyId, memberId)
            .orElseThrow(() -> new NotFoundException("Study Member Not Found"));

        Timer timer = Timer.builder()
            .studyId(studyId)
            .studyMemberId(studyMemberId)
            .dailyStudyTime(req.getStudyTime())
            .build();
        timerRepository.save(timer);
    }
}
