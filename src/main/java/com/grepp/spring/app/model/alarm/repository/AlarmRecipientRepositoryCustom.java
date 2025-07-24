package com.grepp.spring.app.model.alarm.repository;

import com.grepp.spring.app.model.alarm.entity.AlarmRecipient;
import java.util.List;

public interface AlarmRecipientRepositoryCustom {

    List<AlarmRecipient> findMyAlarms(Long memberId);

    void markAllAsRead(Long memberId);

}
