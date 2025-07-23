package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.member.code.StudyRole;
import com.grepp.spring.app.model.member.entity.StudyMember;
import java.util.List;
import java.util.Optional;

public interface StudyMemberRepositoryCustom {

    List<Long> findAllStudies(Long memberId);

    Optional<Long> findIdByStudyMemberIdAndMemberId(Long studyId,Long memberId);

    List<StudyMember> findAllByStudyIdWithMember(Long studyId);

    Optional<StudyRole> findRoleByStudyAndMember(Long studyId, Long memberId);

    Optional<Long> findStudyMemberIdByStudyIdWithMemberId(Long studyId, Long memberId);

    Boolean checkAcceptorHasRight(Long acceptorId, Long studyId);

}
