package com.grepp.studium.attendance.repos;

import com.grepp.studium.attendance.domain.Attendance;
import com.grepp.studium.study_member.domain.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    Attendance findFirstByStudyMember(StudyMember studyMember);

}
