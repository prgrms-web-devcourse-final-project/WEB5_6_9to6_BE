package com.grepp.spring.app.model.member.repository;

import com.grepp.spring.app.model.study.entity.Attendance;
import com.grepp.spring.app.model.member.entity.StudyMember;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyAttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByStudyMemberAndAttendanceDate(StudyMember studyMember, LocalDate attendanceDate);

    List<Attendance> findByStudyMember_StudyMemberIdAndAttendanceDateBetween(Long studyMemberId, LocalDate startDate, LocalDate endDate);

}
