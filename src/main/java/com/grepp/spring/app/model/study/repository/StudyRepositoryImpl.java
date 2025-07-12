package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.controller.api.study.payload.StudySearchRequest;
import com.grepp.spring.app.model.study.code.Category;
import com.grepp.spring.app.model.study.code.Region;
import com.grepp.spring.app.model.study.code.Status;
import com.grepp.spring.app.model.study.entity.Study;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.util.StringUtils;

import java.util.List;

public class StudyRepositoryImpl implements StudyRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

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
        if (StringUtils.hasText(req.getName())) {
            query.setParameter("name", "%" + req.getName() + "%");
        }

        return query.getResultList();
    }

}