package com.grepp.spring.app.model.studyMmeber.repository;

import static com.grepp.spring.app.model.member.entity.QStudyMember.studyMember;

import com.grepp.spring.app.model.auth.code.Role;
import com.grepp.spring.app.model.studyMmeber.code.StudyRole;
import com.grepp.spring.app.model.member.entity.QMember;
import com.grepp.spring.app.model.member.entity.QStudyMember;
import com.grepp.spring.app.model.studyMmeber.entity.StudyMember;
import com.grepp.spring.app.model.study.entity.QStudy;
import com.grepp.spring.app.model.study.entity.QStudySchedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudyMemberRepositoryCustomImpl implements StudyMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final QStudyMember sm = studyMember;
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
            .orderBy(
                sm.studyRole.asc(),
                sm.createdAt.asc(),
                sm.studyMemberId.asc()
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
            .select(sm.studyRole)
            .from(sm)
            .where(sm.study.studyId.eq(studyId), sm.member.id.eq(memberId), sm.activated.isTrue())
            .fetchOne();
        return Optional.ofNullable(res);
    }

    @Override
    public Boolean checkAcceptorHasRight(Long memberId, Long studyId) {
        Boolean result = queryFactory
            .select(sm.studyRole.eq(StudyRole.LEADER).or(sm.member.role.eq(Role.ROLE_ADMIN)))
            .from(sm)
            .where(
                sm.study.studyId.eq(studyId),
                sm.member.id.eq(memberId),
                sm.activated.isTrue(),
                sm.member.activated.isTrue()
            )
            .fetchFirst();

        return Boolean.TRUE.equals(result);
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
    public Optional<StudyMember> findByStudyIdAndMemberId(Long studyId, Long memberId) {
        return Optional.ofNullable(
            queryFactory.selectFrom(studyMember)
                .where(
                    studyMember.study.studyId.eq(studyId),
                    studyMember.member.id.eq(memberId)
                )
                .fetchOne()
        );
    }

    @Override
    public int countByStudyId(Long studyId) {
        Long count = queryFactory
            .select(sm.count())
            .from(sm)
            .where(
                sm.study.studyId.eq(studyId),
                sm.activated.isTrue(),
                sm.member.activated.isTrue(),
                sm.study.activated.isTrue()
            )
            .fetchOne();
        return count != null ? count.intValue() : 0;
    }

    @Override
    public boolean existStudyMember(Long memberId, Long studyId) {
        Integer result = queryFactory
            .selectOne()
            .from(sm)
            .where(
                sm.study.studyId.eq(studyId),
                sm.member.id.eq(memberId),
                sm.activated.isTrue()
            )
            .fetchFirst();
        return result != null;
    }

    @Override
    public boolean existSurvivalMember(Long memberId, Long studyId) {
        Integer result = queryFactory
            .selectOne()
            .from(sm)
            .where(
                sm.study.studyId.eq(studyId),
                sm.member.id.eq(memberId)
            )
            .fetchFirst();
        return result != null;
    }

    @Override
    public Optional<StudyMember> findByStudyIdAndStudyMemberId(Long studyId, Long studyMemberId) {
        StudyMember result = queryFactory
            .selectFrom(sm)
            .where(
                sm.study.studyId.eq(studyId),
                sm.studyMemberId.eq(studyMemberId)
            )
            .fetchOne();
        return Optional.ofNullable(result);
    }


//    @Override
//    public boolean existStudyMember(Long memberId, Long studyId) {
//        return queryFactory
//            .selectOne()
//            .from(sm)
//            .where(
//                sm.member.id.eq(memberId),
//                sm.study.studyId.eq(studyId),
//                sm.activated.isTrue(),
//                sm.member.activated.isTrue(),
//                sm.study.activated.isTrue()
//            )
//            .fetchFirst() != null;
//    }

}
