package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.Region;
import com.grepp.spring.app.model.study.code.Status;
import com.grepp.spring.app.model.study.entity.Study;
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
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static com.grepp.spring.app.model.study.entity.QStudy.study;

@Slf4j
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
                (req.getCategory() != null && req.getCategory() != Category.ALL) ? study.category.eq(req.getCategory()) : null,
                (req.getRegion() != null && req.getRegion() != Region.ALL) ? study.region.eq(req.getRegion()) : null,
                (req.getStatus() != null && req.getStatus() != Status.ALL) ? study.status.eq(req.getStatus()) : null,
                (req.getStudyType() != null ) ? study.studyType.eq(req.getStudyType()) : null,
                StringUtils.hasText(req.getName()) ? study.name.contains(req.getName()) : null,
                study.activated.isTrue()
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

}