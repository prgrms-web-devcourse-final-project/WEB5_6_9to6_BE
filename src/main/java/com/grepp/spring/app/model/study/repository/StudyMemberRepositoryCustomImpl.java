package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.member.code.StudyRole;
import com.grepp.spring.app.model.member.entity.QMember;
import com.grepp.spring.app.model.member.entity.QStudyMember;
import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.study.entity.QStudy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyMemberRepositoryCustomImpl implements StudyMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final QStudyMember studyMember = QStudyMember.studyMember;
    private static final QStudy study = QStudy.study;
    private static final QMember member = QMember.member;

    @Override
    public List<Long> findAllStudies(Long memberId) {
        return queryFactory
            .select(studyMember.studyMemberId)
            .from(studyMember)
            .where(
                studyMember.member.id.eq(memberId)
                    .and(studyMember.activated.isTrue())
            )
            .fetch();
    }

    @Override
    public Optional<Long> findStudyMemberId(Long studyId, Long memberId) {
        Long id =  queryFactory
            .select(studyMember.studyMemberId)
            .from(studyMember)
            .where(
                studyMember.study.studyId.eq(studyId),
                studyMember.member.id.eq(memberId),
                studyMember.activated.isTrue()
            )
            .fetchOne();
        return Optional.ofNullable(id);
    }

    @Override
    public List<StudyMember> findByStudyId(Long studyId) {
        return queryFactory
            .selectFrom(studyMember)
            .join(studyMember.member, member).fetchJoin()
            .where(
                studyMember.study.studyId.eq(studyId),
                studyMember.activated.isTrue()
            )
            .orderBy(
                studyMember.studyRole.asc(),
                studyMember.createdAt.asc()
            )
            .fetch();
    }

    @Override
    public Optional<StudyMember> findStudyMember(Long studyId, Long memberId) {
        StudyMember res = queryFactory
            .select(studyMember)
            .from(studyMember)
            .where(
                studyMember.study.studyId.eq(studyId),
                studyMember.member.id.eq(memberId),
                studyMember.member.activated.isTrue(),
                studyMember.study.activated.isTrue()
            )
            .fetchOne();
        return Optional.ofNullable(res);
    }

    @Override
    public Optional<StudyRole> findStudyRole(Long studyId, Long memberId) {
        StudyRole res = queryFactory
            .select(studyMember.studyRole)
            .from(studyMember)
            .where(
                studyMember.study.studyId.eq(studyId),
                studyMember.member.id.eq(memberId),
                studyMember.activated.isTrue()
            )
            .fetchOne();
        return Optional.ofNullable(res);
    }

    @Override
    public Boolean checkAcceptorHasRight(Long acceptorId, Long studyId) {
        return queryFactory
            .select(studyMember.studyRole.eq(StudyRole.LEADER))
            .from(studyMember)
            .where(
                studyMember.study.studyId.eq(studyId),
                studyMember.member.id.eq(acceptorId),
                studyMember.activated.isTrue(),
                studyMember.member.activated.isTrue()
            )
            .fetchOne();
    }

    @Override
    public boolean existStudyMember(Long memberId, Long studyId) {
        return queryFactory
            .selectOne()
            .from(studyMember)
            .where(
                studyMember.member.id.eq(memberId),
                studyMember.study.studyId.eq(studyId),
                studyMember.activated.isTrue(),
                studyMember.member.activated.isTrue(),
                studyMember.study.activated.isTrue()
            )
            .fetchFirst() != null;
    }

}
