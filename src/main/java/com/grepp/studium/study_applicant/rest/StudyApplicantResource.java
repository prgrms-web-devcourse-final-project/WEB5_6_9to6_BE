package com.grepp.studium.study_applicant.rest;

import com.grepp.studium.study_applicant.model.StudyApplicantDTO;
import com.grepp.studium.study_applicant.service.StudyApplicantService;
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
@RequestMapping(value = "/api/studyApplicants", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyApplicantResource {

    private final StudyApplicantService studyApplicantService;

    public StudyApplicantResource(final StudyApplicantService studyApplicantService) {
        this.studyApplicantService = studyApplicantService;
    }

    @GetMapping
    public ResponseEntity<List<StudyApplicantDTO>> getAllStudyApplicants() {
        return ResponseEntity.ok(studyApplicantService.findAll());
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<StudyApplicantDTO> getStudyApplicant(
            @PathVariable(name = "applicationId") final Integer applicationId) {
        return ResponseEntity.ok(studyApplicantService.get(applicationId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createStudyApplicant(
            @RequestBody @Valid final StudyApplicantDTO studyApplicantDTO) {
        final Integer createdApplicationId = studyApplicantService.create(studyApplicantDTO);
        return new ResponseEntity<>(createdApplicationId, HttpStatus.CREATED);
    }

    @PutMapping("/{applicationId}")
    public ResponseEntity<Integer> updateStudyApplicant(
            @PathVariable(name = "applicationId") final Integer applicationId,
            @RequestBody @Valid final StudyApplicantDTO studyApplicantDTO) {
        studyApplicantService.update(applicationId, studyApplicantDTO);
        return ResponseEntity.ok(applicationId);
    }

    @DeleteMapping("/{applicationId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStudyApplicant(
            @PathVariable(name = "applicationId") final Integer applicationId) {
        studyApplicantService.delete(applicationId);
        return ResponseEntity.noContent().build();
    }

}
