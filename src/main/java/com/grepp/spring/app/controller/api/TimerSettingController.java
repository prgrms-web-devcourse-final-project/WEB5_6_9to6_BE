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
@RequestMapping(value = "/api/timerSettings", produces = MediaType.APPLICATION_JSON_VALUE)
public class TimerSettingController {

//    private final TimerSettingService timerSettingService;
//
//    public TimerSettingController(final TimerSettingService timerSettingService) {
//        this.timerSettingService = timerSettingService;
//    }
//
//    @GetMapping
//    public ResponseEntity<List<TimerSettingDTO>> getAllTimerSettings() {
//        return ResponseEntity.ok(timerSettingService.findAll());
//    }
//
//    @GetMapping("/{settingId}")
//    public ResponseEntity<TimerSettingDTO> getTimerSetting(
//            @PathVariable(name = "settingId") final Integer settingId) {
//        return ResponseEntity.ok(timerSettingService.get(settingId));
//    }
//
//    @PostMapping
//    @ApiResponse(responseCode = "201")
//    public ResponseEntity<Integer> createTimerSetting(
//            @RequestBody @Valid final TimerSettingDTO timerSettingDTO) {
//        final Integer createdSettingId = timerSettingService.create(timerSettingDTO);
//        return new ResponseEntity<>(createdSettingId, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{settingId}")
//    public ResponseEntity<Integer> updateTimerSetting(
//            @PathVariable(name = "settingId") final Integer settingId,
//            @RequestBody @Valid final TimerSettingDTO timerSettingDTO) {
//        timerSettingService.update(settingId, timerSettingDTO);
//        return ResponseEntity.ok(settingId);
//    }
//
//    @DeleteMapping("/{settingId}")
//    @ApiResponse(responseCode = "204")
//    public ResponseEntity<Void> deleteTimerSetting(
//            @PathVariable(name = "settingId") final Integer settingId) {
//        timerSettingService.delete(settingId);
//        return ResponseEntity.noContent().build();
//    }

}
