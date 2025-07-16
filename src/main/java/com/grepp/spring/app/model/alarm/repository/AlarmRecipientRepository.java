package com.grepp.spring.app.model.alarm.repository;

import com.grepp.spring.app.model.alarm.entity.AlarmRecipient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRecipientRepository extends JpaRepository<AlarmRecipient, Long> {

    List<AlarmRecipient> findByMember_Id(Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE AlarmRecipient ar SET ar.isRead = true WHERE ar.member.id = :memberId AND ar.isRead = false")
    int markAllAsReadByMemberId(@Param("memberId") Long memberId);

}
