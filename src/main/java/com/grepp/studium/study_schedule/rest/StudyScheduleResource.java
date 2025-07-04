package com.grepp.studium.study_schedule.rest;

import com.grepp.studium.study_schedule.model.StudyScheduleDTO;
import com.grepp.studium.study_schedule.service.StudyScheduleService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/studySchedules", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyScheduleResource {

    private final StudyScheduleService studyScheduleService;

    public StudyScheduleResource(final StudyScheduleService studyScheduleService) {
        this.studyScheduleService = studyScheduleService;
    }

    @GetMapping
    public ResponseEntity<List<StudyScheduleDTO>> getAllStudySchedules() {
        return ResponseEntity.ok(studyScheduleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyScheduleDTO> getStudySchedule(
            @PathVariable(name = "id") final String id) {
        return ResponseEntity.ok(studyScheduleService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createStudySchedule(
            @RequestBody @Valid final StudyScheduleDTO studyScheduleDTO) {
        final String createdId = studyScheduleService.create(studyScheduleDTO);
        return new ResponseEntity<>('"' + createdId + '"', HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudySchedule(@PathVariable(name = "id") final String id,
            @RequestBody @Valid final StudyScheduleDTO studyScheduleDTO) {
        studyScheduleService.update(id, studyScheduleDTO);
        return ResponseEntity.ok('"' + id + '"');
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStudySchedule(@PathVariable(name = "id") final String id) {
        studyScheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
