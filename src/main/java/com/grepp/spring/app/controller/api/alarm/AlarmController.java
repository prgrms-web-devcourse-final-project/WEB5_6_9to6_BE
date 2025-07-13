package com.grepp.spring.app.controller.api.alarm;

import com.grepp.spring.app.model.alarm.dto.AlarmListResponse;
import com.grepp.spring.app.model.alarm.service.AlarmService;
import com.grepp.spring.infra.response.CommonResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/alarms", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlarmController {

    private AlarmService alarmService;

    // 멤버 알람 목록
    @GetMapping("/{memberId}")
    public ResponseEntity<CommonResponse<List<AlarmListResponse>>> getMemberAlarms(@PathVariable Long memberId) {
        List<AlarmListResponse> alarms = alarmService.getAlarmsByMemberId(memberId);
        return ResponseEntity.ok(CommonResponse.success(alarms));
    }


    // 알람 전송
    @PostMapping()
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> sendAlarm(
        @RequestBody AlarmSendRequest req
    ) {
        return ResponseEntity.status(200).body(
            CommonResponse.noContent()
        );
    }

    // 알람 목록
    @GetMapping()
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> getAlarms() {

        long alarmId1 = 1;
        long alarmId2 = 2;
        LocalDateTime sentAt1 = LocalDateTime.now().minusDays(1);
        LocalDateTime sentAt2 = LocalDateTime.now();

        List<?> list = List.of(
            Map.of("alarmId", alarmId1, "type", AlarmType.REJECT, "message", AlarmType.REJECT.message, "sentAt", sentAt1),
            Map.of("alarmId", alarmId2, "type", AlarmType.ACCEPT, "message", AlarmType.ACCEPT.message, "sentAt", sentAt2)
        );

        return ResponseEntity.status(200).body(
            CommonResponse.success(list)
        );
    }

    // 알람 읽음 처리
    @PatchMapping("/{alarmRecipientId}/read")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> readAlarm(
        @PathVariable long alarmRecipientId
    ) {
        String message = "알림을 읽음처리 하였습니다.";

        return ResponseEntity.status(200).body(
            CommonResponse.noContent()
        );
    }

    @Getter
    private enum AlarmType {
        APPLY("스터디 가입 요청이 발생했습니다"),
        ACCEPT("스터디 가입 요청이 수락되었습니다"),
        REJECT("스터디 가입 요청이 거절되었습니다");

        private final String message;

        AlarmType(String message) {
            this.message = message;
        }

    }

    private static class AlarmSendRequest {
        public long senderId;
        public long receiverId;
        public AlarmType type;
        public String message;
    }
}
