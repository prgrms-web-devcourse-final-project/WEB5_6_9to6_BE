package com.grepp.studium.alarm.repos;

import com.grepp.studium.alarm.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
}
