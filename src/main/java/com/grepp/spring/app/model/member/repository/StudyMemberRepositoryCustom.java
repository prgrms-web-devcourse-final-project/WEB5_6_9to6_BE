package com.grepp.spring.app.model.member.repository;

import com.grepp.spring.app.model.member.code.StudyRole;
import com.grepp.spring.app.model.study.entity.StudyMember;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepositoryCustom {

    List<Long> findAllStudies(Long memberId);

    List<StudyMember> findByStudyId(Long studyId);

    Optional<StudyMember> findStudyMember(Long studyId, Long memberId);

    Optional<StudyRole> findStudyRole(Long studyId, Long memberId);

    Optional<Long> findStudyMemberId(Long studyId, Long memberId);

    Boolean checkAcceptorHasRight(Long acceptorId, Long studyId);

    List<StudyMember> findActiveStudyMemberships(Long memberId);

    Optional<StudyMember> findActiveStudyMember(Long memberId, Long studyId);

    List<StudyMember> findAllByStudyIdWithMember(Long studyId);

    Optional<StudyMember> findByStudyIdAndMemberId(Long studyId, Long memberId);

    int countByStudyId(Long studyId);

    boolean existStudyMember(Long memberId, Long studyId);

    boolean existSurvivalMember(Long memberId, Long studyId);

    Optional<StudyMember> findByStudyIdAndStudyMemberId(Long studyId, Long studyMemberId);

//    boolean existStudyMember(Long memberId, Long studyId);
}
