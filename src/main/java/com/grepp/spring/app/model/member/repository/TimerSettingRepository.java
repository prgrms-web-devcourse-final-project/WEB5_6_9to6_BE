package com.grepp.spring.app.model.member.repository;

import com.grepp.spring.app.model.member.entity.TimerSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerSettingRepository extends JpaRepository<TimerSetting, Long> {

}
