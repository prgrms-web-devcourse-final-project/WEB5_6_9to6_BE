package com.grepp.studium.study_member.repos;

import com.grepp.studium.member.domain.Member;
import com.grepp.studium.study.domain.Study;
import com.grepp.studium.study_member.domain.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudyMemberRepository extends JpaRepository<StudyMember, Integer> {

    StudyMember findFirstByMember(Member member);

    StudyMember findFirstByStudy(Study study);

}
