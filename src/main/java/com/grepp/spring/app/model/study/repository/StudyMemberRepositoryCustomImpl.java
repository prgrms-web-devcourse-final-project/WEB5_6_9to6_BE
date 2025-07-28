package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.member.code.StudyRole;
import com.grepp.spring.app.model.member.entity.QMember;
import com.grepp.spring.app.model.member.entity.QStudyMember;
import com.grepp.spring.app.model.member.entity.StudyMember;
import com.grepp.spring.app.model.study.entity.QStudy;
import com.grepp.spring.app.model.study.entity.QStudySchedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyMemberRepositoryCustomImpl implements StudyMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final QStudyMember sm = QStudyMember.studyMember;
    private static final QStudy s = QStudy.study;
    private static final QMember m = QMember.member;
    private static final QStudySchedule sch = QStudySchedule.studySchedule;

    @Override
    public List<Long> findAllStudies(Long memberId) {
        return queryFactory
            .select(sm.studyMemberId)
            .from(sm)
            .where(sm.member.id.eq(memberId).and(sm.activated.isTrue()))
            .fetch();
    }

    @Override
    public Optional<Long> findStudyMemberId(Long studyId, Long memberId) {
        Long id = queryFactory
            .select(sm.studyMemberId)
            .from(sm)
            .where(sm.study.studyId.eq(studyId), sm.member.id.eq(memberId), sm.activated.isTrue())
            .fetchOne();
        return Optional.ofNullable(id);
    }

    @Override
    public List<StudyMember> findByStudyId(Long studyId) {
        return queryFactory
            .selectFrom(sm)
            .join(sm.member, m).fetchJoin()
            .where(sm.study.studyId.eq(studyId), sm.activated.isTrue())
            .orderBy(sm.studyRole.asc(), sm.createdAt.asc())
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
            .select(sm.studyRole)
            .from(sm)
            .where(sm.study.studyId.eq(studyId), sm.member.id.eq(memberId), sm.activated.isTrue())
            .fetchOne();
        return Optional.ofNullable(res);
    }

    @Override
    public Boolean checkAcceptorHasRight(Long acceptorId, Long studyId) {
        return queryFactory
            .select(sm.studyRole.eq(StudyRole.LEADER))
            .from(sm)
            .where(
                sm.study.studyId.eq(studyId),
                sm.member.id.eq(acceptorId),
                sm.activated.isTrue(),
                sm.member.activated.isTrue()
            )
            .fetchOne();
    }

    @Override
    public List<StudyMember> findActiveStudyMemberships(Long memberId) {
        return queryFactory
            .selectFrom(sm)
            .join(sm.study, s).fetchJoin()
            .leftJoin(s.schedules, sch).fetchJoin()
            .where(
                sm.member.id.eq(memberId),
                sm.activated.isTrue(),
                s.activated.isTrue()
            )
            .distinct()
            .fetch();
    }

    @Override
    public Optional<StudyMember> findActiveStudyMember(Long memberId, Long studyId) {
        StudyMember result = queryFactory
            .selectFrom(sm)
            .join(sm.study, s).fetchJoin()
            .where(
                sm.member.id.eq(memberId),
                s.studyId.eq(studyId),
                s.activated.isTrue()
            )
            .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<StudyMember> findAllByStudyIdWithMember(Long studyId) {
        return queryFactory
            .selectFrom(sm)
            .join(sm.member, m).fetchJoin()
            .where(sm.study.studyId.eq(studyId), sm.activated.isTrue())
            .fetch();
    }

    @Override
    public boolean existStudyMember(Long memberId, Long studyId) {
        return queryFactory
            .selectOne()
            .from(sm)
            .where(
                sm.member.id.eq(memberId),
                sm.study.studyId.eq(studyId),
                sm.activated.isTrue(),
                sm.member.activated.isTrue(),
                sm.study.activated.isTrue()
            )
            .fetchFirst() != null;
    }

}
