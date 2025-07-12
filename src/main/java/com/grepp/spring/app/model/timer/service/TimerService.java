package com.grepp.spring.app.model.timer.service;

import com.grepp.spring.app.model.member.repository.StudyMemberRepository;
import com.grepp.spring.app.model.timer.dto.DailyStudyLogResponse;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class TimerService {

    private final TimerRepository timerRepository;
    private final TimerQueryRepository timerQueryRepository;
    private final StudyMemberRepository studyMemberRepository;

    public Long getAllStudyTime(Long memberId) {
        List<Long> studyMemberIds = studyMemberRepository.findAllStudies(memberId);

        if (studyMemberIds == null || studyMemberIds.isEmpty()) {
            log.info("studyMemberIds is null or empty");
            return 0L;
        }
        log.info("studyMemberIds: {}", studyMemberIds);
        Long totalStudyTime = timerRepository.findTotalStudyTimeByStudyMemberIds(studyMemberIds);

        return totalStudyTime != null ? totalStudyTime : 0L;
    }

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

    public void recordStudyTime(Long studyId, Long studyMemberId) {

    }
}
