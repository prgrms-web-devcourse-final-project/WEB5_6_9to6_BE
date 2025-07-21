package com.grepp.spring.app.model.timer.repository;
import com.grepp.spring.app.model.timer.dto.DailyStudyLogResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.querydsl.core.Tuple;

import static com.grepp.spring.app.model.timer.entity.QTimer.*;


@Repository
@RequiredArgsConstructor
public class TimerQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<Tuple> findDailyStudyLogsByStudyMemberId(Long studyMemberId, Long studyId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return queryFactory
            .select(
                Expressions.dateTemplate(LocalDate.class, "DATE({0})", timer.createdAt),
                timer.dailyStudyTime.sum().castToNum(Long.class)
            )
            .from(timer)
            .where(
                timer.studyMemberId.eq(studyMemberId),
                timer.createdAt.goe(startOfDay),
                timer.createdAt.lt(endOfDay),
                timer.studyId.eq(studyId)
            )
            .groupBy(Expressions.dateTemplate(LocalDate.class, "DATE({0})", timer.createdAt))
            .fetch();
    }

    public Long findTotalStudyTimeInPeriod(Long studyMemberId, Long studyId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return queryFactory
            .select(timer.dailyStudyTime.sum().castToNum(Long.class))
            .from(timer)
            .where(
                timer.studyMemberId.eq(studyMemberId),
                timer.studyId.eq(studyId),
                timer.createdAt.goe(startOfDay),
                timer.createdAt.lt(endOfDay)
            )

            .fetchOne();
    }

    public Long findTotalStdTimeByStdMemberIds(List<Long> studyMemberIds) {
        return queryFactory
            .select(timer.dailyStudyTime.sum().castToNum(Long.class))
            .from(timer)
            .where(timer.studyMemberId.in(studyMemberIds), timer.activated.eq(true))
            .fetchOne();
    }

}
