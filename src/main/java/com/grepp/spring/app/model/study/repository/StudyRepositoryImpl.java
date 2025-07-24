package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.Region;
import com.grepp.spring.app.model.study.code.Status;
import com.grepp.spring.app.model.study.code.StudyType;
import com.grepp.spring.app.model.study.entity.Study;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import static com.grepp.spring.app.model.member.entity.QMember.member;
import static com.grepp.spring.app.model.study.entity.QApplicant.applicant;
import static com.grepp.spring.app.model.study.entity.QStudy.study;

@Slf4j
@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Study> searchStudiesPage(StudySearchRequest req) {
        return queryFactory
            .selectFrom(study)
            .distinct()
            .leftJoin(study.schedules).fetchJoin()
            .where(
                study.activated.isTrue(),
                req.getCategory() != null && req.getCategory() != Category.ALL ? study.category.eq(req.getCategory()) : null,
                req.getRegion() != null && req.getRegion() != Region.ALL ? study.region.eq(req.getRegion()) : null,
                req.getStatus() != null && req.getStatus() != Status.ALL ? study.status.eq(req.getStatus()) : null,
                req.getStudyType() != null ? study.studyType.eq(req.getStudyType()) : null,
                StringUtils.hasText(req.getName()) ? study.name.contains(req.getName()) : null
            )
            .orderBy(study.createdAt.desc())
            .fetch();
    }


    @Override
    public Page<Study> searchStudiesPage(StudySearchRequest req, Pageable pageable) {
        List<Long> ids = queryFactory
            .select(study.studyId)
            .from(study)
            .where(
                study.activated.isTrue(),
                (req.getCategory() != null && req.getCategory() != Category.ALL) ? study.category.eq(req.getCategory()) : null,
                (req.getRegion() != null && req.getRegion() != Region.ALL) ? study.region.eq(req.getRegion()) : null,
                (req.getStatus() != null && req.getStatus() != Status.ALL) ? study.status.eq(req.getStatus()) : null,
                (req.getStudyType() != null ) ? study.studyType.eq(req.getStudyType()) : null,
                StringUtils.hasText(req.getName()) ? study.name.contains(req.getName()) : null
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(
                study.createdAt.desc(),
                study.studyId.desc()
            )
            .fetch();

        log.debug("Finding studies for page: {}, IDs: {}", pageable.getPageNumber(), ids);

        if (ids.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Study> content = queryFactory
            .selectFrom(study)
            .distinct()
            .leftJoin(study.schedules).fetchJoin()
            .where(study.studyId.in(ids))
            .orderBy(
                study.createdAt.desc(),
                study.studyId.desc()
            )
            .where(study.activated.isTrue(),study.studyId.in(ids))
            .orderBy(study.createdAt.desc())
            .fetch();

        Long total = queryFactory
            .select(study.count())
            .from(study)
            .where(
                study.activated.isTrue(),
                (req.getCategory() != null && req.getCategory() != Category.ALL) ? study.category.eq(req.getCategory()) : null,
                (req.getRegion() != null && req.getRegion() != Region.ALL) ? study.region.eq(req.getRegion()) : null,
                (req.getStatus() != null && req.getStatus() != Status.ALL) ? study.status.eq(req.getStatus()) : null,
                (req.getStudyType() != null ) ? study.studyType.eq(req.getStudyType()) : null,
                StringUtils.hasText(req.getName()) ? study.name.contains(req.getName()) : null
            )
            .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    @Override
    public Optional<Study> findByIdWithSchedulesAndGoals(Long studyId) {
        String jpql = """
            SELECT s FROM Study s
            LEFT JOIN FETCH s.schedules
            LEFT JOIN FETCH s.goals
            WHERE s.studyId = :studyId
            """;
        return em.createQuery(jpql, Study.class)
            .setParameter("studyId", studyId)
            .getResultList()
            .stream()
            .findFirst();
    }

    @Override
    public List<ApplicantsResponse> findApplicants(Long studyId) {
        return queryFactory
            .select(Projections.constructor(ApplicantsResponse.class,
                applicant.id,
                member.id,
                member.nickname,
                applicant.state,
                applicant.introduction,
                member.avatarImage))
            .from(applicant)
            .join(applicant.member, member)
            .where(
                applicant.study.studyId.eq(studyId),
                applicant.activated.isTrue(),
                applicant.member.activated.isTrue()
            )
            .orderBy(
                applicant.createdAt.asc()
            )
            .fetch();
    }

    @Override
    public Optional<Study> findWithGoals(Long id) {
        Study res = queryFactory
            .selectFrom(study)
            .leftJoin(study.goals).fetchJoin()
            .where(
                study.studyId.eq(id),
                study.activated.isTrue()
            )
            .fetchOne();
        return Optional.ofNullable(res);
    }

    @Override
    public Optional<Study> findWithStudySchedules(Long id) {
        Study res = queryFactory
            .select(study)
            .from(study)
            .leftJoin(study.schedules).fetchJoin()
            .where(
                study.studyId.eq(id),
                study.activated.isTrue()
            )
            .fetchOne();
        return Optional.ofNullable(res);
    }

    @Override
    public StudyType findStudyType(Long studyId) {
        return queryFactory
            .select(study.studyType)
            .from(study)
            .where(study.studyId.eq(studyId))
            .fetchOne();
    }

    @Override
    public Optional<String> findNotice(Long studyId) {
        String res = queryFactory
            .select(study.notice)
            .from(study)
            .where(
                study.studyId.eq(studyId)
            )
            .fetchOne();
        return Optional.ofNullable(res);
    }

}