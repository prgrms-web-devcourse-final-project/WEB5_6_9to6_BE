package com.grepp.studium.quiz_record.rest;

import com.grepp.studium.quiz_record.model.QuizRecordDTO;
import com.grepp.studium.quiz_record.service.QuizRecordService;
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
@RequestMapping(value = "/api/quizRecords", produces = MediaType.APPLICATION_JSON_VALUE)
public class QuizRecordResource {

    private final QuizRecordService quizRecordService;

    public QuizRecordResource(final QuizRecordService quizRecordService) {
        this.quizRecordService = quizRecordService;
    }

    @GetMapping
    public ResponseEntity<List<QuizRecordDTO>> getAllQuizRecords() {
        return ResponseEntity.ok(quizRecordService.findAll());
    }

    @GetMapping("/{quizRecordId}")
    public ResponseEntity<QuizRecordDTO> getQuizRecord(
            @PathVariable(name = "quizRecordId") final Integer quizRecordId) {
        return ResponseEntity.ok(quizRecordService.get(quizRecordId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createQuizRecord(
            @RequestBody @Valid final QuizRecordDTO quizRecordDTO) {
        final Integer createdQuizRecordId = quizRecordService.create(quizRecordDTO);
        return new ResponseEntity<>(createdQuizRecordId, HttpStatus.CREATED);
    }

    @PutMapping("/{quizRecordId}")
    public ResponseEntity<Integer> updateQuizRecord(
            @PathVariable(name = "quizRecordId") final Integer quizRecordId,
            @RequestBody @Valid final QuizRecordDTO quizRecordDTO) {
        quizRecordService.update(quizRecordId, quizRecordDTO);
        return ResponseEntity.ok(quizRecordId);
    }

    @DeleteMapping("/{quizRecordId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteQuizRecord(
            @PathVariable(name = "quizRecordId") final Integer quizRecordId) {
        quizRecordService.delete(quizRecordId);
        return ResponseEntity.noContent().build();
    }

}
