package com.grepp.spring.app.model.member.repository;

import com.grepp.spring.app.model.member.entity.Timer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {

    @Query("select sum(t.dailyStudyTime) from Timer t where t.studyMemberId = :studyMemberId")
    int findSumOfAllByStudyMemberId(Long studyMemberId);

    @Query("select sum(t.dailyStudyTime) from Timer t where t.studyMemberId in :studyMemberIds")
    Long findTotalStudyTimeByStudyMemberIds(List<Long> studyMemberIds);

}
