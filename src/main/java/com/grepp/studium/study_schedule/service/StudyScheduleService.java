package com.grepp.studium.study_schedule.service;

import com.grepp.studium.study.domain.Study;
import com.grepp.studium.study.repos.StudyRepository;
import com.grepp.studium.study_schedule.domain.StudySchedule;
import com.grepp.studium.study_schedule.model.StudyScheduleDTO;
import com.grepp.studium.study_schedule.repos.StudyScheduleRepository;
import com.grepp.studium.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StudyScheduleService {

    private final StudyScheduleRepository studyScheduleRepository;
    private final StudyRepository studyRepository;

    public StudyScheduleService(final StudyScheduleRepository studyScheduleRepository,
            final StudyRepository studyRepository) {
        this.studyScheduleRepository = studyScheduleRepository;
        this.studyRepository = studyRepository;
    }

    public List<StudyScheduleDTO> findAll() {
        final List<StudySchedule> studySchedules = studyScheduleRepository.findAll(Sort.by("id"));
        return studySchedules.stream()
                .map(studySchedule -> mapToDTO(studySchedule, new StudyScheduleDTO()))
                .toList();
    }

    public StudyScheduleDTO get(final String id) {
        return studyScheduleRepository.findById(id)
                .map(studySchedule -> mapToDTO(studySchedule, new StudyScheduleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public String create(final StudyScheduleDTO studyScheduleDTO) {
        final StudySchedule studySchedule = new StudySchedule();
        mapToEntity(studyScheduleDTO, studySchedule);
        studySchedule.setId(studyScheduleDTO.getId());
        return studyScheduleRepository.save(studySchedule).getId();
    }

    public void update(final String id, final StudyScheduleDTO studyScheduleDTO) {
        final StudySchedule studySchedule = studyScheduleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(studyScheduleDTO, studySchedule);
        studyScheduleRepository.save(studySchedule);
    }

    public void delete(final String id) {
        studyScheduleRepository.deleteById(id);
    }

    private StudyScheduleDTO mapToDTO(final StudySchedule studySchedule,
            final StudyScheduleDTO studyScheduleDTO) {
        studyScheduleDTO.setId(studySchedule.getId());
        studyScheduleDTO.setDayOfWeek(studySchedule.getDayOfWeek());
        studyScheduleDTO.setStartTime(studySchedule.getStartTime());
        studyScheduleDTO.setEndTime(studySchedule.getEndTime());
        studyScheduleDTO.setStudy(studySchedule.getStudy() == null ? null : studySchedule.getStudy().getStudyId());
        return studyScheduleDTO;
    }

    private StudySchedule mapToEntity(final StudyScheduleDTO studyScheduleDTO,
            final StudySchedule studySchedule) {
        studySchedule.setDayOfWeek(studyScheduleDTO.getDayOfWeek());
        studySchedule.setStartTime(studyScheduleDTO.getStartTime());
        studySchedule.setEndTime(studyScheduleDTO.getEndTime());
        final Study study = studyScheduleDTO.getStudy() == null ? null : studyRepository.findById(studyScheduleDTO.getStudy())
                .orElseThrow(() -> new NotFoundException("study not found"));
        studySchedule.setStudy(study);
        return studySchedule;
    }

    public boolean idExists(final String id) {
        return studyScheduleRepository.existsByIdIgnoreCase(id);
    }

}
