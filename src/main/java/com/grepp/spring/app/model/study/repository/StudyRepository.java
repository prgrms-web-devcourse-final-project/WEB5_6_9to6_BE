package com.grepp.spring.app.model.study.repository;

import com.grepp.spring.app.model.study.entity.Study;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long>, StudyRepositoryCustom {

    List<Study> findAllByEndDateBefore(LocalDate date);

    boolean existsByStudyIdAndActivatedTrue(Long studyId);

    Optional<Study> findByStudyIdAndActivatedTrue(Long studyId);

}
