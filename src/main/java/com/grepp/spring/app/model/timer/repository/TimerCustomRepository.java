package com.grepp.spring.app.model.timer.repository;

import com.querydsl.core.Tuple;
import java.time.LocalDateTime;
import java.util.List;

public interface TimerCustomRepository {

    List<Tuple> findDailyStudyLogs(Long studyMemberId, Long studyId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    Long findTotalStudyLogsInWeek(Long studyMemberId, Long studyId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    Long findTotalStudyTime(List<Long> studyMemberIds);

}
