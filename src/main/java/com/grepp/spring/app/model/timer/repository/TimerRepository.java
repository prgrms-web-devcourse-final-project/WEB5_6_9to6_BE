package com.grepp.spring.app.model.timer.repository;

import com.grepp.spring.app.model.timer.entity.Timer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long>, TimerCustomRepository {

}
