package com.grepp.studium.timer_setting.repos;

import com.grepp.studium.timer_setting.domain.TimerSetting;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TimerSettingRepository extends JpaRepository<TimerSetting, Integer> {
}
