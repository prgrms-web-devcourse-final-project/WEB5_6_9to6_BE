package com.grepp.spring.app.model.timer.repository;

import com.querydsl.core.Tuple;
import java.time.LocalDateTime;
import java.util.List;

public interface TimerCustomRepository {

    List<Tuple> findDailyStudyLogsByStudyMemberId(Long studyMemberId, Long studyId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    Long findTotalStudyTimeInPeriod(Long studyMemberId, Long studyId, LocalDateTime startOfDay, LocalDateTime endOfDay);

}
