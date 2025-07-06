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
@RequestMapping(value = "/api/attendances", produces = MediaType.APPLICATION_JSON_VALUE)
public class AttendanceController {

//    private final AttendanceService attendanceService;
//
//    public AttendanceController(final AttendanceService attendanceService) {
//        this.attendanceService = attendanceService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<AttendanceDTO>> getAllAttendances() {
//        return ResponseEntity.ok(attendanceService.findAll());
//    }
//
//    @GetMapping("/{attendanceId}")
//    public ResponseEntity<AttendanceDTO> getAttendance(
//            @PathVariable(name = "attendanceId") final Integer attendanceId) {
//        return ResponseEntity.ok(attendanceService.get(attendanceId));
//    }
//
//    @PostMapping
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Integer> createAttendance(
//            @RequestBody @Valid final AttendanceDTO attendanceDTO) {
//        final Integer createdAttendanceId = attendanceService.create(attendanceDTO);
//        return new ResponseEntity<>(createdAttendanceId, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{attendanceId}")
//    public ResponseEntity<Integer> updateAttendance(
//            @PathVariable(name = "attendanceId") final Integer attendanceId,
//            @RequestBody @Valid final AttendanceDTO attendanceDTO) {
//        attendanceService.update(attendanceId, attendanceDTO);
//        return ResponseEntity.ok(attendanceId);
//    }
//
//    @DeleteMapping("/{attendanceId}")
//    @ApiResponse(responseCode = "204")
//    public ResponseEntity<Void> deleteAttendance(
//            @PathVariable(name = "attendanceId") final Integer attendanceId) {
//        attendanceService.delete(attendanceId);
//        return ResponseEntity.noContent().build();
//    }

}
