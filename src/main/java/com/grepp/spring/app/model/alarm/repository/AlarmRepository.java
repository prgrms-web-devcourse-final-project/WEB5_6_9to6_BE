package com.grepp.spring.app.model.alarm.repository;

import com.grepp.spring.app.model.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

}
