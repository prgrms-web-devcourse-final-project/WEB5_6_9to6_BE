package com.grepp.studium.quiz_record.repos;

import com.grepp.studium.quiz_record.domain.QuizRecord;
import com.grepp.studium.quiz_set.domain.QuizSet;
import com.grepp.studium.study_member.domain.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuizRecordRepository extends JpaRepository<QuizRecord, Integer> {

    QuizRecord findFirstByStudyMember(StudyMember studyMember);

    QuizRecord findFirstByQuizSet(QuizSet quizSet);

}
