package com.grepp.studium.timer.repos;

import com.grepp.studium.study_member.domain.StudyMember;
import com.grepp.studium.timer.domain.Timer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TimerRepository extends JpaRepository<Timer, Integer> {

    Timer findFirstByStudyMember(StudyMember studyMember);

}
