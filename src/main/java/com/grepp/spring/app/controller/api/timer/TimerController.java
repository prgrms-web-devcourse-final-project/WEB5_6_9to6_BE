package com.grepp.spring.app.controller.api.timer;

import com.grepp.spring.app.controller.api.timer.payload.StudyTimeRecordRequest;
import com.grepp.spring.app.model.timer.dto.DailyStudyLogResponse;
import com.grepp.spring.app.model.timer.service.TimerService;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.SuccessCode;
import com.grepp.spring.infra.util.SecurityUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/timer")
@RequiredArgsConstructor
public class TimerController {

    private final TimerService timerService;

    // 스터디별 공부시간 등록
    @PostMapping("/{studyId}/{studyMemberId}")
    public ResponseEntity<CommonResponse<SuccessCode>> recordStudyTime(
        @PathVariable Long studyId, @PathVariable Long studyMemberId,
        @RequestBody StudyTimeRecordRequest req
    ) {
       timerService.recordStudyTime(studyId, studyMemberId, req);
       return ResponseEntity.ok(CommonResponse.success(SuccessCode.SUCCESS));
    }

    // 타이머 전체 누적 시간 조회
    @GetMapping("/{memberId}/all-time")
    public ResponseEntity<CommonResponse<Long>> getAllTimer(@PathVariable Long memberId) {
        log.info("getAllTimer memberId: {}", memberId);
        return ResponseEntity.ok(CommonResponse.success(timerService.getAllStudyTime(memberId)));
    }

    // 스터디별 7일간의 스터디별 공부시간 확인
    @GetMapping("/{studyId}/study-time")
    public ResponseEntity<CommonResponse<List<DailyStudyLogResponse>>> getTimerAtStudy(@PathVariable Long studyId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<DailyStudyLogResponse> response = timerService.findDailyStudyLogsByStudyMemberId(studyId, memberId);
        return ResponseEntity.ok(CommonResponse.success(response));
    }


}
