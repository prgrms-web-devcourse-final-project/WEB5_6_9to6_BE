package com.grepp.spring.app.controller.api.timer;

import com.grepp.spring.app.controller.api.timer.payload.StudyTimeRecordRequest;
import com.grepp.spring.app.model.timer.dto.DailyStudyLogResponse;
import com.grepp.spring.app.model.timer.dto.StudyWeekTimeResponse;
import com.grepp.spring.app.model.timer.dto.TotalStudyTimeResponse;
import com.grepp.spring.app.model.timer.service.TimerService;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.SuccessCode;
import com.grepp.spring.infra.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@Tag(name = "타이머 API", description = "공부 시간 측정 관련 API 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/v1/timer")
@RequiredArgsConstructor
public class TimerController {

    private final TimerService timerService;

    // 스터디별 공부시간 등록
    @Operation(
        summary = "공부시간 등록",
        description = """
        특정 스터디에서 맴버가 공부한 시간 `studyTime`을 int로 받아 저장합니다.
        해당 요청은 성공메시지를 반환하며 `data`에 특별한 값은 없습니다.    
        """
    )
    @PostMapping("/{studyId}")
    public ResponseEntity<CommonResponse<SuccessCode>> recordStudyTime(
        @PathVariable Long studyId,
        @Valid @RequestBody StudyTimeRecordRequest req
    ) {
        Long memberId = SecurityUtil.getCurrentMemberId();
       timerService.recordStudyTime(studyId, memberId, req);
       return ResponseEntity.ok(CommonResponse.noContent(SuccessCode.SUCCESS));
    }

    // 타이머 전체 누적 시간 조회
    @Operation(
        summary = "전체 누적 시간 조회",
        description = """
        uri의 memberId를 이용해 해당 맴버의 전체 누적 공부시간을 조회합니다.
        반환 값은 Long 타입입니다. ex) "data": { "totalStudyTime" : 10000}
        """
    )
    @GetMapping("/{memberId}/all-time")
    public ResponseEntity<CommonResponse<TotalStudyTimeResponse>> getAllTimer(@PathVariable Long memberId) {
        log.info("getAllTimer memberId: {}", memberId);
         TotalStudyTimeResponse res= timerService.getAllStudyTime(memberId);
        return ResponseEntity.ok(CommonResponse.success(res));
    }

    // 스터디별 7일간의 스터디별 공부시간 확인
    @Operation(
        summary = "최근 7일간의 일별 공부시간 확인",
        description = """
        로그인된 사용자의 특정 스터디에서의 최근 7일간의 일별 공부 시간을 조회합니다.

        - 결과는 날짜와 시간이 리스트형태로 반환됩니다. ex) "data": [  {"studyDate":"2025-07-12", "dailyTotalStudyTime" : 10}  ] 
        - 토큰 정보가 없으면 조회가 불가능합니다.
        """
    )
    @GetMapping("/{studyId}/week-daily-time")
    public ResponseEntity<CommonResponse<List<DailyStudyLogResponse>>> getDailyTimeAtStudy(@PathVariable Long studyId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        List<DailyStudyLogResponse> response = timerService.findDailyStudyLogsByStudyMemberId(studyId, memberId);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    // 스터디별 7일간의 누적 공부 시간 확인
    @Operation(
        summary = "최근 7일간의 누적 공부시간 확인",
        description = """
        로그인된 사용자의 특정 스터디에서의 최근 7일간의 총 누적 공부 시간을 조회합니다.

        - 결과는 누적 시간으로 반환됩니다. ex) "data": {"weekTotalStudyTime": 100}
        - 토큰 정보가 없으면 조회가 불가능합니다.
        """
    )
    @GetMapping("/{studyId}/week-all-time")
    public ResponseEntity<CommonResponse<StudyWeekTimeResponse>> getAllTimeAtStudy(@PathVariable Long studyId) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        StudyWeekTimeResponse response = timerService.getStudyTimeForPeriod(studyId, memberId);
        return ResponseEntity.ok(CommonResponse.success(response));
    }


}
