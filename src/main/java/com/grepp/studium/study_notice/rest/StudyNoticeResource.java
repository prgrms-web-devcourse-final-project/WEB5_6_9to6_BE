package com.grepp.studium.study_notice.rest;

import com.grepp.studium.study_notice.model.StudyNoticeDTO;
import com.grepp.studium.study_notice.service.StudyNoticeService;
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
@RequestMapping(value = "/api/studyNotices", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudyNoticeResource {

    private final StudyNoticeService studyNoticeService;

    public StudyNoticeResource(final StudyNoticeService studyNoticeService) {
        this.studyNoticeService = studyNoticeService;
    }

    @GetMapping
    public ResponseEntity<List<StudyNoticeDTO>> getAllStudyNotices() {
        return ResponseEntity.ok(studyNoticeService.findAll());
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<StudyNoticeDTO> getStudyNotice(
            @PathVariable(name = "noticeId") final Integer noticeId) {
        return ResponseEntity.ok(studyNoticeService.get(noticeId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createStudyNotice(
            @RequestBody @Valid final StudyNoticeDTO studyNoticeDTO) {
        final Integer createdNoticeId = studyNoticeService.create(studyNoticeDTO);
        return new ResponseEntity<>(createdNoticeId, HttpStatus.CREATED);
    }

    @PutMapping("/{noticeId}")
    public ResponseEntity<Integer> updateStudyNotice(
            @PathVariable(name = "noticeId") final Integer noticeId,
            @RequestBody @Valid final StudyNoticeDTO studyNoticeDTO) {
        studyNoticeService.update(noticeId, studyNoticeDTO);
        return ResponseEntity.ok(noticeId);
    }

    @DeleteMapping("/{noticeId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStudyNotice(
            @PathVariable(name = "noticeId") final Integer noticeId) {
        studyNoticeService.delete(noticeId);
        return ResponseEntity.noContent().build();
    }

}
