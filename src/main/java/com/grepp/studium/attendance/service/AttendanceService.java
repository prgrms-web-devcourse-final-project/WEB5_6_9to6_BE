package com.grepp.studium.attendance.service;

import com.grepp.studium.attendance.domain.Attendance;
import com.grepp.studium.attendance.model.AttendanceDTO;
import com.grepp.studium.attendance.repos.AttendanceRepository;
import com.grepp.studium.study_member.domain.StudyMember;
import com.grepp.studium.study_member.repos.StudyMemberRepository;
import com.grepp.studium.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudyMemberRepository studyMemberRepository;

    public AttendanceService(final AttendanceRepository attendanceRepository,
            final StudyMemberRepository studyMemberRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studyMemberRepository = studyMemberRepository;
    }

    public List<AttendanceDTO> findAll() {
        final List<Attendance> attendances = attendanceRepository.findAll(Sort.by("attendanceId"));
        return attendances.stream()
                .map(attendance -> mapToDTO(attendance, new AttendanceDTO()))
                .toList();
    }

    public AttendanceDTO get(final Integer attendanceId) {
        return attendanceRepository.findById(attendanceId)
                .map(attendance -> mapToDTO(attendance, new AttendanceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final AttendanceDTO attendanceDTO) {
        final Attendance attendance = new Attendance();
        mapToEntity(attendanceDTO, attendance);
        return attendanceRepository.save(attendance).getAttendanceId();
    }

    public void update(final Integer attendanceId, final AttendanceDTO attendanceDTO) {
        final Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(attendanceDTO, attendance);
        attendanceRepository.save(attendance);
    }

    public void delete(final Integer attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }

    private AttendanceDTO mapToDTO(final Attendance attendance, final AttendanceDTO attendanceDTO) {
        attendanceDTO.setAttendanceId(attendance.getAttendanceId());
        attendanceDTO.setAttendanceDate(attendance.getAttendanceDate());
        attendanceDTO.setIsAttended(attendance.getIsAttended());
        attendanceDTO.setActivated(attendance.getActivated());
        attendanceDTO.setStudyMember(attendance.getStudyMember() == null ? null : attendance.getStudyMember().getStudyMemberId());
        return attendanceDTO;
    }

    private Attendance mapToEntity(final AttendanceDTO attendanceDTO, final Attendance attendance) {
        attendance.setAttendanceDate(attendanceDTO.getAttendanceDate());
        attendance.setIsAttended(attendanceDTO.getIsAttended());
        attendance.setActivated(attendanceDTO.getActivated());
        final StudyMember studyMember = attendanceDTO.getStudyMember() == null ? null : studyMemberRepository.findById(attendanceDTO.getStudyMember())
                .orElseThrow(() -> new NotFoundException("studyMember not found"));
        attendance.setStudyMember(studyMember);
        return attendance;
    }

}
