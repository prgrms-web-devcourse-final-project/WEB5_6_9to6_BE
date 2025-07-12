package com.grepp.spring.app.model.member.repository;

import com.grepp.spring.app.model.member.entity.StudyMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    @Query("select sm.studyMemberId from StudyMember sm where sm.member.id = :memberId")
    List<Long> findAllStudies(Long memberId);

}
