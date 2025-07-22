package com.grepp.spring.app.model.alarm.service;

import com.grepp.spring.app.controller.api.alarm.payload.AlarmRequest;
import com.grepp.spring.app.model.alarm.code.AlarmType;
import com.grepp.spring.app.model.alarm.dto.AlarmListResponse;
import com.grepp.spring.app.model.alarm.entity.Alarm;
import com.grepp.spring.app.model.alarm.entity.AlarmRecipient;
import com.grepp.spring.app.model.alarm.repository.AlarmRecipientRepository;
import com.grepp.spring.app.model.alarm.repository.AlarmRepository;
import com.grepp.spring.app.model.alarm.sse.EmitterRepository;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import com.grepp.spring.infra.error.exceptions.alarm.AlarmValidationException;
import com.grepp.spring.infra.response.ResponseCode;
import com.grepp.spring.infra.util.SecurityUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final AlarmRecipientRepository alarmRecipientRepository;
    private final MemberRepository memberRepository;
    private final EmitterRepository emitterRepository;

    // 알림 생성, 전송
    @Transactional
    public void createAndSendAlarm(AlarmRequest request) {

        if (request.getSenderId().equals(request.getReceiverId())) {
            throw new AlarmValidationException(ResponseCode.ALARM_SENDER_EQUALS_RECEIVER);
        }
        // result의 경우, resultStatus 필요
        if (request.getType() == AlarmType.RESULT && request.getResultStatus() == null) {
            throw new AlarmValidationException(ResponseCode.ALARM_RESULT_STATUS_REQUIRED);
        }
        // apply의 경우, resultStatus 불가
        if (request.getType() == AlarmType.APPLY && request.getResultStatus() != null) {
            throw new AlarmValidationException(ResponseCode.ALARM_RESULT_STATUS_NOT_ALLOWED);
        }

        Member sender = memberRepository.findById(request.getSenderId())
            .orElseThrow(() -> new NotFoundException("알림을 보낸 사용자가 존재하지 않습니다."));
        Member receiver = memberRepository.findById(request.getReceiverId())
            .orElseThrow(() -> new NotFoundException("알림을 받는 사용자가 존재하지 않습니다."));

        // 알림 저장
        Alarm alarm = Alarm.builder()
            .sender(sender)
            .receiver(receiver)
            .message(request.getMessage())
            .alarmType(request.getType())
            .resultStatus(request.getResultStatus()) // APPLY일 경우 null이어도 됨
            .build();
        alarmRepository.save(alarm);

        AlarmRecipient recipient = new AlarmRecipient(alarm, receiver);
        alarmRecipientRepository.save(recipient);

        // SSE 실시간 전송
        SseEmitter emitter = emitterRepository.get(receiver.getId());
        if (emitter != null) {

            try {
                Map<String, Object> payload = new HashMap<>();
                payload.put("alarmRecipientId", recipient.getId());
                payload.put("type", alarm.getAlarmType().name());
                payload.put("message", alarm.getMessage());
                payload.put("sentAt", alarm.getCreatedAt());
                payload.put("isRead", recipient.getIsRead());

                if (alarm.getAlarmType() == AlarmType.RESULT && alarm.getResultStatus() != null) {
                    payload.put("resultStatus", alarm.getResultStatus().name());
                }
                emitter.send(SseEmitter.event().name("alarm").data(payload));
            } catch (IOException e) {
                log.warn("알림 전송 실패 - 수신자 ID: {}, 사유: {}", receiver.getId(), e.getMessage());
                emitterRepository.remove(receiver.getId());
            }
        }
    }

    // 알림 목록 조회
    @Transactional(readOnly = true)
    public List<AlarmListResponse> getAlarmsByMemberId(Long memberId) {
        List<AlarmRecipient> recipients = alarmRecipientRepository.findAllWithSenderByMemberId(memberId);

        return recipients.stream()
            .map(AlarmListResponse::new)
            .collect(Collectors.toList());
    }

    // 알림 개별 읽음 처리
    @Transactional
    public void markAlarmAsRead(Long alarmRecipientId) {
        AlarmRecipient recipient = alarmRecipientRepository.findById(alarmRecipientId)
            .orElseThrow(() -> new NotFoundException("알림 수신 정보를 찾을 수 없습니다."));

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        if (!recipient.getMember().getId().equals(currentMemberId)) {
            throw new AlarmValidationException(ResponseCode.ALARM_ACCESS_DENIED);
        }

        if (!recipient.getIsRead()) {
            recipient.markAsRead();
        }
    }

    // 알림 모두 읽음 처리
    @Transactional
    public void markAllAlarmsAsRead(Long memberId) {
        alarmRecipientRepository.markAllAsReadByMemberId(memberId);
    }
}