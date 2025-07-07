package com.grepp.spring.app.controller.api;

import com.grepp.spring.infra.util.ReferencedException;
import com.grepp.spring.infra.util.ReferencedWarning;
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
@RequestMapping(value = "/api/alarms", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlarmController {

    public AlarmController() {

    }

//    @GetMapping
//    public ResponseEntity<List<AlarmDTO>> getAllAlarms() {
//        return ResponseEntity.ok(alarmService.findAll());
//    }

//    @GetMapping("/{alarmId}")
//    public ResponseEntity<AlarmDTO> getAlarm(
//            @PathVariable(name = "alarmId") final Integer alarmId) {
//        return ResponseEntity.ok(alarmService.get(alarmId));
//    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createAlarm() {
        return null;
    }

    @PutMapping("/{alarmId}")
    public ResponseEntity<Integer> updateAlarm(
) {
        return null;
    }

    @DeleteMapping("/{alarmId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAlarm() {

        return ResponseEntity.noContent().build();
    }

}
