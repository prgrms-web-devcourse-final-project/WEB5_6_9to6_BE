package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long>, StudyRepositoryCustom {

}

