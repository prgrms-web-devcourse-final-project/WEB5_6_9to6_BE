package com.grepp.spring.app.model.alarm.repository;

import com.grepp.spring.app.model.alarm.entity.AlarmRecipient;
import com.grepp.spring.app.model.alarm.entity.QAlarm;
import com.grepp.spring.app.model.alarm.entity.QAlarmRecipient;
import com.grepp.spring.app.model.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class AlarmRecipientRepositoryImpl implements AlarmRecipientRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AlarmRecipient> findMyAlarms(Long memberId) {

        QAlarmRecipient ar = QAlarmRecipient.alarmRecipient;
        QAlarm a = QAlarm.alarm;
        QMember m = QMember.member;

        return queryFactory
            .selectFrom(ar)
            .join(ar.alarm, a).fetchJoin()
            .leftJoin(a.sender, m).fetchJoin()
            .where(ar.member.id.eq(memberId))
            .orderBy(a.createdAt.desc())
            .fetch();
    }

    @Override
    @Transactional
    public void markAllAsRead(Long memberId) {
        QAlarmRecipient ar = QAlarmRecipient.alarmRecipient;

        queryFactory
            .update(ar)
            .set(ar.isRead, true)
            .where(ar.member.id.eq(memberId).and(ar.isRead.isFalse()))
            .execute();
    }
}
