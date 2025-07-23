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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static com.grepp.spring.app.model.member.entity.QMember.member;
import static com.grepp.spring.app.model.study.entity.QApplicant.applicant;
import static com.grepp.spring.app.model.study.entity.QStudy.study;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Study> searchByFilterWithSchedules(StudySearchRequest req) {
        StringBuilder jpql = new StringBuilder(
            "SELECT DISTINCT s FROM Study s LEFT JOIN FETCH s.schedules WHERE 1=1");

        if (req.getCategory() != null && req.getCategory() != Category.ALL) {
            jpql.append(" AND s.category = :category");
        }
        if (req.getRegion() != null && req.getRegion() != Region.ALL) {
            jpql.append(" AND s.region = :region");
        }
        if (req.getStatus() != null && req.getStatus() != Status.ALL) {
            jpql.append(" AND s.status = :status");
        }
        if (req.getStudyType() != null) {
            jpql.append(" AND s.studyType = :studyType");
        }
        if (StringUtils.hasText(req.getName())) {
            jpql.append(" AND s.name LIKE :name");
        }

        TypedQuery<Study> query = em.createQuery(jpql.toString(), Study.class);

        if (req.getCategory() != null && req.getCategory() != Category.ALL) {
            query.setParameter("category", req.getCategory());
        }
        if (req.getRegion() != null && req.getRegion() != Region.ALL) {
            query.setParameter("region", req.getRegion());
        }
        if (req.getStatus() != null && req.getStatus() != Status.ALL) {
            query.setParameter("status", req.getStatus());
        }
        if (req.getStudyType() != null) {
            query.setParameter("studyType", req.getStudyType());
        }
        if (StringUtils.hasText(req.getName())) {
            query.setParameter("name", "%" + req.getName() + "%");
        }

        return query.getResultList();
    }

    @Override
    public Page<Study> searchByFilterWithSchedules(StudySearchRequest req, Pageable pageable) {
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
            .orderBy(study.createdAt.desc())
            .fetch();

        if (ids.isEmpty()) {
            return Page.empty(pageable);
        }

        List<Study> content = queryFactory
            .selectFrom(study)
            .distinct()
            .leftJoin(study.schedules).fetchJoin()
            .where(
                study.studyId.in(ids),
                study.activated.isTrue()
            )
            .orderBy(study.createdAt.desc())
            .fetch();

        Long total = queryFactory
            .select(study.count())
            .from(study)
            .where(
                (req.getCategory() != null && req.getCategory() != Category.ALL) ? study.category.eq(req.getCategory()) : null,
                (req.getRegion() != null && req.getRegion() != Region.ALL) ? study.region.eq(req.getRegion()) : null,
                (req.getStatus() != null && req.getStatus() != Status.ALL) ? study.status.eq(req.getStatus()) : null,
                (req.getStudyType() != null ) ? study.studyType.eq(req.getStudyType()) : null,
                StringUtils.hasText(req.getName()) ? study.name.contains(req.getName()) : null
            )
            .fetchFirst();

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
    public List<ApplicantsResponse> findAllApplicants(Long studyId) {
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
                applicant.activated.isTrue()
            )
            .fetch();
    }

    @Override
    public Optional<Study> findByIdWithGoals(Long id) {
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
    public Optional<Study> findByIdWithSchedules(Long id) {
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
    public StudyType findStudyTypeById(Long studyId) {
        return queryFactory
            .select(study.studyType)
            .from(study)
            .where(study.studyId.eq(studyId))
            .fetchOne();
    }

    @Override
    public Optional<String> findNoticeByStudyId(Long studyId) {
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