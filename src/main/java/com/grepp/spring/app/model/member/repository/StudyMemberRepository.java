package com.grepp.spring.app.model.member.repository;

import com.grepp.spring.app.model.member.entity.StudyMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {

    List<StudyMember> findByMemberId(Long memberId);

    @Query("select sm.studyMemberId from StudyMember sm where sm.member.id = :memberId")
    List<Long> findAllStudies(Long memberId);

    @Query("select sm.studyMemberId from StudyMember sm where sm.studyMemberId = :studyMemberId and sm.member.id = :memberId")
    Optional<Long> findIdByStudyMemberIdAndMemberId(Long studyMemberId, Long memberId);

    Optional<StudyMember> findByStudyStudyIdAndMemberId(Long studyId, Long memberId);

    Optional<StudyMember> findByMember_IdAndStudy_StudyId(Long memberId, Long studyId);

    int countByStudy_StudyId(Long studyId);
}
