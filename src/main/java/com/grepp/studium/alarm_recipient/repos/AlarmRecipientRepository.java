package com.grepp.studium.alarm_recipient.repos;

import com.grepp.studium.alarm.domain.Alarm;
import com.grepp.studium.alarm_recipient.domain.AlarmRecipient;
import com.grepp.studium.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlarmRecipientRepository extends JpaRepository<AlarmRecipient, Integer> {

    AlarmRecipient findFirstByAlarm(Alarm alarm);

    AlarmRecipient findFirstByMember(Member member);

}
