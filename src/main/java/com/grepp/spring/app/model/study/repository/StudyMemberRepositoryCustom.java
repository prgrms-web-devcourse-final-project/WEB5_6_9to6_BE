package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.member.code.StudyRole;
import com.grepp.spring.app.model.member.entity.StudyMember;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface StudyMemberRepositoryCustom {

    List<Long> findAllStudies(@Param("memberId") Long memberId);

    Optional<Long> findIdByStudyMemberIdAndMemberId(Long studyId,Long memberId);

    List<StudyMember> findAllByStudyIdWithMember(Long studyId);

    Optional<StudyRole> findRoleByStudyAndMember(Long studyId, Long memberId);

    Optional<Long> findStudyMemberIdByStudyIdWithMeberId(Long studyId, Long memberId);

    Boolean isAcceptorHasRight(@Param("acceptorId") Long acceptorId, Long studyId);

}
