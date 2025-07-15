package com.grepp.spring.app.model.member.repository;

import com.grepp.spring.app.model.member.code.StudyRole;
import com.grepp.spring.app.model.member.entity.StudyMember;
import io.lettuce.core.dynamic.annotation.Param;
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

    boolean existsByMember_IdAndStudy_StudyId(Long memberId, Long studyId);

    @Query("SELECT sm FROM StudyMember sm JOIN FETCH sm.member WHERE sm.study.studyId = :studyId")
    List<StudyMember> findAllByStudyIdWithMember(@Param("studyId") Long studyId);


    @Query("select sm.studyRole  from StudyMember sm "
        + "where sm.study.studyId = :studyId and sm.member.id = :memberId")
    Optional<StudyRole> findRoleByStudyAndMember(Long studyId, Long memberId);
}
