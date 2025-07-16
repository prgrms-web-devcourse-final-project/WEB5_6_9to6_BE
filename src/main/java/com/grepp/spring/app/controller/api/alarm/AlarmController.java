package com.grepp.spring.app.controller.api.alarm;

import com.grepp.spring.app.controller.api.alarm.payload.AlarmRequest;
import com.grepp.spring.app.model.alarm.dto.AlarmListResponse;
import com.grepp.spring.app.model.alarm.service.AlarmService;
import com.grepp.spring.app.model.alarm.sse.EmitterRepository;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "알림 API", description = "실시간 알림(SSE) 및 알림 목록 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/alarms")
public class AlarmController {

    private final AlarmService alarmService;
    private final EmitterRepository emitterRepository;

    // 멤버와 SSE 연결
    @Operation(
        summary = "알림 구독 (SSE 연결)",
        description = """
        클라이언트가 실시간 알림을 받기 위해 서버와 SSE(Server-Sent Events) 연결을 맺습니다.
        - `memberId`를 경로 변수로 받아 해당 사용자의 Emitter를 생성하고 저장합니다.
        - `produces`는 `text/event-stream`으로, 실시간으로 데이터가 스트리밍됨을 의미합니다.
        - 연결이 타임아웃되거나 완료되면 서버에서 자동으로 Emitter가 제거됩니다.
        """
    )
    @GetMapping(value = "/subscribe/{memberId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long memberId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitterRepository.save(memberId, emitter);

        emitter.onTimeout(() -> emitterRepository.remove(memberId));
        emitter.onCompletion(() -> emitterRepository.remove(memberId));

        return emitter;
    }

    // 알림 전송
    @Operation(
        summary = "알림 전송 (내부용)",
        description = """
        요청 body에 `AlarmRequest`를 포함하여야 합니다.
        서버에서 특정 이벤트가 발생했을 때, 사용자에게 알림을 전송하기 위해 호출되는 API입니다.
        - `AlarmRequest`에 담긴 정보를 바탕으로 알림을 생성하고, 구독된 클라이언트(SSE)에게 메시지를 전송합니다.
        - 주로 서버 내부 로직에서 호출되는 것을 상정합니다.
        """
    )
    @PostMapping
    public ResponseEntity<CommonResponse<Void>> sendAlarm(@RequestBody AlarmRequest request) {
        alarmService.createAndSendAlarm(request);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(CommonResponse.noContent(SuccessCode.ALARM_SENT));
    }

    // 알림 읽음 처리
    @Operation(
        summary = "알림 읽음 처리",
        description = "알림 수신 ID(`alarmRecipientId`)를 받아 해당하는 알림을 '읽음' 상태로 변경합니다."
    )
    @PatchMapping("/{alarmRecipientId}/read")
    public ResponseEntity<CommonResponse<Void>> markAlarmAsRead(@PathVariable Long alarmRecipientId) {
        alarmService.markAlarmAsRead(alarmRecipientId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(CommonResponse.noContent(SuccessCode.ALARM_READ));
    }

    // 알림 목록 조회
    @Operation(
        summary = "사용자의 알림 목록 조회",
        description = "사용자 ID(`memberId`)를 이용하여 해당 사용자가 받은 모든 알림의 목록을 최신순으로 조회합니다."
    )
    @GetMapping("/{memberId}")
    public ResponseEntity<CommonResponse<List<AlarmListResponse>>> getMemberAlarms(@PathVariable Long memberId) {
        List<AlarmListResponse> alarms = alarmService.getAlarmsByMemberId(memberId);
        return ResponseEntity.ok(CommonResponse.success(alarms));
    }
}
