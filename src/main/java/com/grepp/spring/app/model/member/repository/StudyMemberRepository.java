package com.grepp.spring.app.model.member.repository;

import com.grepp.spring.app.model.member.entity.StudyMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyMemberRepository extends JpaRepository<StudyMember, Long>, StudyMemberRepositoryCustom {

    Optional<StudyMember> findByStudy_StudyIdAndStudyMemberId(Long studyId, Long studyMemberId);
}
