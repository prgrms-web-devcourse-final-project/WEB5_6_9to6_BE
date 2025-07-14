package com.grepp.spring.app.model.alarm.service;

import com.grepp.spring.app.model.alarm.dto.AlarmListResponse;
import com.grepp.spring.app.model.alarm.entity.Alarm;
import com.grepp.spring.app.model.alarm.entity.AlarmRecipient;
import com.grepp.spring.app.model.alarm.repository.AlarmRecipientRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRecipientRepository alarmRecipientRepository;

    @Transactional(readOnly = true)
    public List<AlarmListResponse> getAlarmsByMemberId(Long memberId) {
        List<AlarmRecipient> recipients = alarmRecipientRepository.findByMemberId(memberId);

        return recipients.stream()
            .map(recipient -> {
                Alarm alarm = recipient.getAlarm();
                return AlarmListResponse.builder()
                    .alarmId(alarm.getId())
                    .type(alarm.getAlarmType().name())
                    .message(alarm.getMessage())
                    .isRead(recipient.getIsRead())
                    .sentAt(alarm.getCreatedAt())
                    .build();
            })
            .collect(Collectors.toList());
    }
}
