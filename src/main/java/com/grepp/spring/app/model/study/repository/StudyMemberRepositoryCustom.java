package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.member.code.StudyRole;
import com.grepp.spring.app.model.member.entity.StudyMember;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepositoryCustom {

    List<Long> findAllStudies(Long memberId);

    List<StudyMember> findByStudyId(Long studyId);

    Optional<StudyRole> findStudyRole(Long studyId, Long memberId);

    Optional<Long> findStudyMemberId(Long studyId, Long memberId);

    Boolean checkAcceptorHasRight(Long acceptorId, Long studyId);


    List<StudyMember> findActiveStudyMemberships(Long memberId);

    Optional<StudyMember> findActiveStudyMember(Long memberId, Long studyId);

    List<StudyMember> findAllByStudyIdWithMember(Long studyId);

    boolean existStudyMember(Long memberId, Long studyId);
}
