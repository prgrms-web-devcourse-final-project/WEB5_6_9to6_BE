package com.grepp.spring.app.model.timer.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

import static com.grepp.spring.app.model.timer.entity.QTimer.timer;

@RequiredArgsConstructor
public class TimerCustomRepositoryImpl implements TimerCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tuple> findDailyStudyLogs(Long studyMemberId, Long studyId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
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
                timer.studyId.eq(studyId),
                timer.activated.isTrue()
            )
            .groupBy(Expressions.dateTemplate(LocalDate.class, "DATE({0})", timer.createdAt))
            .fetch();
    }

    @Override
    public Long findTotalStudyLogsInWeek(Long studyMemberId, Long studyId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return queryFactory
            .select(timer.dailyStudyTime.sum().castToNum(Long.class))
            .from(timer)
            .where(
                timer.studyMemberId.eq(studyMemberId),
                timer.studyId.eq(studyId),
                timer.createdAt.goe(startOfDay),
                timer.createdAt.lt(endOfDay),
                timer.activated.isTrue()
            )

            .fetchOne();
    }

    @Override
    public Long findTotalStudyTime(List<Long> studyMemberIds) {

        return queryFactory
            .select(timer.dailyStudyTime.sum().castToNum(Long.class))
            .from(timer)
            .where(
                timer.studyMemberId.in(studyMemberIds),
                timer.activated.isTrue()
            )
            .fetchOne();
    }

}
