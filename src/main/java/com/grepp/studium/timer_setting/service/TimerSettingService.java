package com.grepp.studium.timer_setting.service;

import com.grepp.studium.timer_setting.domain.TimerSetting;
import com.grepp.studium.timer_setting.model.TimerSettingDTO;
import com.grepp.studium.timer_setting.repos.TimerSettingRepository;
import com.grepp.studium.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TimerSettingService {

    private final TimerSettingRepository timerSettingRepository;

    public TimerSettingService(final TimerSettingRepository timerSettingRepository) {
        this.timerSettingRepository = timerSettingRepository;
    }

    public List<TimerSettingDTO> findAll() {
        final List<TimerSetting> timerSettings = timerSettingRepository.findAll(Sort.by("settingId"));
        return timerSettings.stream()
                .map(timerSetting -> mapToDTO(timerSetting, new TimerSettingDTO()))
                .toList();
    }

    public TimerSettingDTO get(final Integer settingId) {
        return timerSettingRepository.findById(settingId)
                .map(timerSetting -> mapToDTO(timerSetting, new TimerSettingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final TimerSettingDTO timerSettingDTO) {
        final TimerSetting timerSetting = new TimerSetting();
        mapToEntity(timerSettingDTO, timerSetting);
        return timerSettingRepository.save(timerSetting).getSettingId();
    }

    public void update(final Integer settingId, final TimerSettingDTO timerSettingDTO) {
        final TimerSetting timerSetting = timerSettingRepository.findById(settingId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(timerSettingDTO, timerSetting);
        timerSettingRepository.save(timerSetting);
    }

    public void delete(final Integer settingId) {
        timerSettingRepository.deleteById(settingId);
    }

    private TimerSettingDTO mapToDTO(final TimerSetting timerSetting,
            final TimerSettingDTO timerSettingDTO) {
        timerSettingDTO.setSettingId(timerSetting.getSettingId());
        timerSettingDTO.setFocusTime(timerSetting.getFocusTime());
        timerSettingDTO.setRestTime(timerSetting.getRestTime());
        return timerSettingDTO;
    }

    private TimerSetting mapToEntity(final TimerSettingDTO timerSettingDTO,
            final TimerSetting timerSetting) {
        timerSetting.setFocusTime(timerSettingDTO.getFocusTime());
        timerSetting.setRestTime(timerSettingDTO.getRestTime());
        return timerSetting;
    }

}
