package com.grepp.spring.app.controller.api;


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
@RequestMapping(value = "/api/alarmRecipients", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlarmRecipientController {

//    private final AlarmRecipientService alarmRecipientService;
//
//    public AlarmRecipientController(final AlarmRecipientService alarmRecipientService) {
//        this.alarmRecipientService = alarmRecipientService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<AlarmRecipientDTO>> getAllAlarmRecipients() {
//        return ResponseEntity.ok(alarmRecipientService.findAll());
//    }
//
//    @GetMapping("/{alarmRecipientId}")
//    public ResponseEntity<AlarmRecipientDTO> getAlarmRecipient(
//            @PathVariable(name = "alarmRecipientId") final Integer alarmRecipientId) {
//        return ResponseEntity.ok(alarmRecipientService.get(alarmRecipientId));
//    }
//
//    @PostMapping
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Integer> createAlarmRecipient(
//            @RequestBody @Valid final AlarmRecipientDTO alarmRecipientDTO) {
//        final Integer createdAlarmRecipientId = alarmRecipientService.create(alarmRecipientDTO);
//        return new ResponseEntity<>(createdAlarmRecipientId, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{alarmRecipientId}")
//    public ResponseEntity<Integer> updateAlarmRecipient(
//            @PathVariable(name = "alarmRecipientId") final Integer alarmRecipientId,
//            @RequestBody @Valid final AlarmRecipientDTO alarmRecipientDTO) {
//        alarmRecipientService.update(alarmRecipientId, alarmRecipientDTO);
//        return ResponseEntity.ok(alarmRecipientId);
//    }
//
//    @DeleteMapping("/{alarmRecipientId}")
//    @ApiResponse(responseCode = "204")
//    public ResponseEntity<Void> deleteAlarmRecipient(
//            @PathVariable(name = "alarmRecipientId") final Integer alarmRecipientId) {
//        alarmRecipientService.delete(alarmRecipientId);
//        return ResponseEntity.noContent().build();
//    }

}
