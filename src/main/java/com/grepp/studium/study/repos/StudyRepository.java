package com.grepp.studium.study.repos;

import com.grepp.studium.study.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudyRepository extends JpaRepository<Study, Integer> {
}
