package com.grepp.spring.app.controller.api.alarm;

import com.grepp.spring.app.controller.api.alarm.payload.AlarmRequest;
import com.grepp.spring.app.model.alarm.dto.AlarmListResponse;
import com.grepp.spring.app.model.alarm.service.AlarmService;
import com.grepp.spring.app.model.alarm.sse.EmitterRepository;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.SuccessCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/alarms")
public class AlarmController {

    private final AlarmService alarmService;
    private final EmitterRepository emitterRepository;

    // 멤버와 SSE 연결
    @GetMapping(value = "/subscribe/{memberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long memberId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitterRepository.save(memberId, emitter);

        emitter.onTimeout(() -> emitterRepository.remove(memberId));
        emitter.onCompletion(() -> emitterRepository.remove(memberId));

        return emitter;
    }

    // 알림 전송
    @PostMapping
    public ResponseEntity<CommonResponse<Void>> sendAlarm(@RequestBody AlarmRequest request) {
        alarmService.createAndSendAlarm(request);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(CommonResponse.noContent(SuccessCode.ALARM_SENT));
    }

    // 알림 읽음 처리
    @PatchMapping("/{alarmRecipientId}/read")
    public ResponseEntity<CommonResponse<Void>> markAlarmAsRead(@PathVariable Long alarmRecipientId) {
        alarmService.markAlarmAsRead(alarmRecipientId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(CommonResponse.noContent(SuccessCode.ALARM_READ));
    }

    // 알림 목록 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<CommonResponse<List<AlarmListResponse>>> getMemberAlarms(@PathVariable Long memberId) {
        List<AlarmListResponse> alarms = alarmService.getAlarmsByMemberId(memberId);
        return ResponseEntity.ok(CommonResponse.success(alarms));
    }
}
