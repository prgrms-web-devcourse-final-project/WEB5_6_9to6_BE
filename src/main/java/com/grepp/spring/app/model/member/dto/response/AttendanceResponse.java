package com.grepp.spring.app.model.member.dto.response;

import com.grepp.spring.app.model.member.entity.Attendance;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttendanceResponse {

    private Long attendanceId;
    private LocalDate attendanceDate;
    private String dayOfWeek;
    private boolean isAttend;
    private Long studyMemberId;

    // 엔티티 → DTO 변환용 생성자
    public AttendanceResponse(Attendance attendance) {
        this.attendanceId = attendance.getId();
        this.attendanceDate = attendance.getAttendanceDate();
        this.dayOfWeek = attendance.getAttendanceDate().getDayOfWeek().name(); // 요일 추출
        this.isAttend = attendance.isAttended();
        this.studyMemberId = attendance.getStudyMember().getStudyMemberId();
    }

    @Builder
    public AttendanceResponse(Long attendanceId, LocalDate attendanceDate, String dayOfWeek, boolean isAttend, Long studyMemberId) {
        this.attendanceId = attendanceId;
        this.attendanceDate = attendanceDate;
        this.dayOfWeek = dayOfWeek;
        this.isAttend = isAttend;
        this.studyMemberId = studyMemberId;
    }
}
