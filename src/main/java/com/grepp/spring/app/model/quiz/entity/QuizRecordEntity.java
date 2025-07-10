package com.grepp.spring.app.model.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz_record")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long quizRecordId;

    @Column(nullable = false)
    private long studyMemberId;

    @Column(nullable = false)
    private long quizSetId;

    @Column(nullable = false)
    private boolean isPassed;

    @Column(nullable = false)
    private boolean activated;
}
