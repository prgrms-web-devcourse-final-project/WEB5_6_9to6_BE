package com.grepp.spring.app.model.member.repository;

import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.study.repository.StudyMemberRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyMemberRepository extends JpaRepository<StudyMember, Long>, StudyMemberRepositoryCustom {

    Optional<StudyMember> findByStudyStudyIdAndMemberId(Long studyId, Long memberId);

    int countByStudy_StudyId(Long studyId);

    boolean existsByMember_IdAndStudy_StudyIdAndActivatedTrue(Long memberId, Long studyId);

    Optional<StudyMember> findByStudy_StudyIdAndStudyMemberId(Long studyId, Long studyMemberId);
}
