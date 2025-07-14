package com.grepp.spring.app.model.alarm.repository;

import com.grepp.spring.app.model.alarm.entity.AlarmRecipient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRecipientRepository extends JpaRepository<AlarmRecipient, Long> {

    List<AlarmRecipient> findByMemberId(Long memberId);

}
