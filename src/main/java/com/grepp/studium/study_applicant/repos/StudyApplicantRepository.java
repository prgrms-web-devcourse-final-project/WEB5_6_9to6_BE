package com.grepp.studium.study_applicant.repos;

import com.grepp.studium.member.domain.Member;
import com.grepp.studium.study.domain.Study;
import com.grepp.studium.study_applicant.domain.StudyApplicant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudyApplicantRepository extends JpaRepository<StudyApplicant, Integer> {

    StudyApplicant findFirstByMember(Member member);

    StudyApplicant findFirstByStudy(Study study);

}
