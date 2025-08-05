package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.study.dto.request.StudySearchRequest;
import com.grepp.spring.app.model.member.dto.response.ApplicantsResponse;
import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.Region;
import com.grepp.spring.app.model.study.code.Status;
import com.grepp.spring.app.model.study.code.StudyType;
import com.grepp.spring.app.model.study.dto.response.StudyListResponse;
import com.grepp.spring.app.model.study.entity.Study;
import com.grepp.spring.app.model.study.entity.StudySchedule;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import static com.grepp.spring.app.model.member.entity.QMember.member;
import static com.grepp.spring.app.model.member.entity.QStudyMember.studyMember;
import static com.grepp.spring.app.model.study.entity.QApplicant.applicant;
import static com.grepp.spring.app.model.study.entity.QStudy.study;
import static com.grepp.spring.app.model.study.entity.QStudySchedule.studySchedule;

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
            .orderBy(
                study.createdAt.desc(),
                study.studyId.desc()
            )
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
            .orderBy(
                study.createdAt.desc(),
                study.studyId.desc()
            )
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

    @Override
    public Page<StudyListResponse> searchStudiesWithMemberCount(StudySearchRequest req, Pageable pageable) {

        // 페이징이 적용 studyId 목록
        List<Long> ids = queryFactory
            .select(study.studyId)
            .from(study)
            .where(searchConditions(req))
            .orderBy(study.createdAt.desc(), study.studyId.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        if (CollectionUtils.isEmpty(ids)) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // 스터디 조회
        List<Study> studies = queryFactory
            .selectFrom(study)
            .where(study.studyId.in(ids))
            .orderBy(study.createdAt.desc(), study.studyId.desc())
            .fetch();

        // 멤버 수를 조회, Map으로 변환
        Map<Long, Long> memberCountsMap = queryFactory
            .select(studyMember.study.studyId, studyMember.count())
            .from(studyMember)
            .where(studyMember.study.studyId.in(ids))
            .groupBy(studyMember.study.studyId)
            .fetch()
            .stream()
            .collect(Collectors.toMap(
                tuple -> tuple.get(0, Long.class), // key: studyId
                tuple -> tuple.get(1, Long.class)  // value: member count
            ));

        // 스케줄 정보 조회
        List<StudySchedule> schedules = queryFactory
            .selectFrom(studySchedule)
            .where(studySchedule.study.studyId.in(ids))
            .fetch();

        // 스케줄을 studyId 기준으로 그룹화, Map으로 변환
        Map<Long, List<StudySchedule>> schedulesMap = schedules.stream()
            .collect(Collectors.groupingBy(s -> s.getStudy().getStudyId()));

        // DTO 리스트 생성
        List<StudyListResponse> content = studies.stream()
            .map(s -> {
                int currentMemberCount = memberCountsMap.getOrDefault(s.getStudyId(), 0L).intValue();
                List<StudySchedule> studySchedules = schedulesMap.getOrDefault(s.getStudyId(), Collections.emptyList());
                return StudyListResponse.fromEntity(s, currentMemberCount, studySchedules);
            })
            .collect(Collectors.toList());

        // 전체 카운트 조회
        JPAQuery<Long> countQuery = queryFactory
            .select(study.count())
            .from(study)
            .where(searchConditions(req));

        return new PageImpl<>(content, pageable, countQuery.fetchOne());
    }

    @Override
    public LocalDate findStudyStartDate(Long studyId) {
        return queryFactory
            .select(study.startDate)
            .from(study)
            .where(
                study.studyId.eq(studyId),
                study.activated.isTrue()
            )
            .fetchOne();
    }

    private BooleanExpression[] searchConditions(StudySearchRequest req) {
        return new BooleanExpression[] {
            categoryEq(req.getCategory()),
            regionEq(req.getRegion()),
            statusEq(req.getStatus()),
            studyTypeEq(req.getStudyType()),
            nameContains(req.getName()),
            study.activated.isTrue()
        };
    }

    private BooleanExpression categoryEq(Category category) {
        return (category != null && category != Category.ALL) ? study.category.eq(category) : null;
    }

    private BooleanExpression regionEq(Region region) {
        return (region != null && region != Region.ALL) ? study.region.eq(region) : null;
    }

    private BooleanExpression statusEq(Status status) {
        return (status != null && status != Status.ALL) ? study.status.eq(status) : null;
    }

    private BooleanExpression studyTypeEq(StudyType studyType) {
        return (studyType != null) ? study.studyType.eq(studyType) : null;
    }

    private BooleanExpression nameContains(String name) {
        return StringUtils.hasText(name) ? study.name.contains(name) : null;
    }

}