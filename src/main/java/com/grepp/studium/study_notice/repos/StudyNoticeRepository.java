package com.grepp.studium.study_notice.repos;

import com.grepp.studium.study.domain.Study;
import com.grepp.studium.study_notice.domain.StudyNotice;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudyNoticeRepository extends JpaRepository<StudyNotice, Integer> {

    StudyNotice findFirstByStudy(Study study);

    boolean existsByStudyStudyId(Integer studyId);

}
