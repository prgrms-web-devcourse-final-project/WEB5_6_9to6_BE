package com.grepp.spring.app.model.study.dto;

import com.grepp.spring.app.model.member.dto.response.AttendanceResponse;
import com.grepp.spring.app.model.member.entity.Attendance;
import lombok.Getter;

import java.util.List;

@Getter
public class WeeklyAttendanceResponse {
    private final Long studyMemberId;
    private final List<AttendanceResponse> attendances;

    public WeeklyAttendanceResponse(Long studyMemberId, List<Attendance> attendanceList) {
        this.studyMemberId = studyMemberId;
        this.attendances = attendanceList.stream()
            .map(AttendanceResponse::new)
            .toList();
    }
}
