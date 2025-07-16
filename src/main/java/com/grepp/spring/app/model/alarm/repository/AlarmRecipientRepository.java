package com.grepp.spring.app.model.alarm.repository;

import com.grepp.spring.app.model.alarm.entity.AlarmRecipient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRecipientRepository extends JpaRepository<AlarmRecipient, Long> {

    @Query("""
            SELECT ar
            FROM AlarmRecipient ar
            JOIN FETCH ar.alarm a
            LEFT JOIN FETCH a.sender s
            WHERE ar.member.id = :memberId
            ORDER BY a.createdAt DESC
    """)
    List<AlarmRecipient> findAllWithSenderByMemberId(@Param("memberId") Long memberId);

}
