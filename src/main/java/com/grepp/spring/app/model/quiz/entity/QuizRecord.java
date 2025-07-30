package com.grepp.spring.app.model.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studyMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quizSetId")
    private QuizSet quizSet;

    @Column(name = "is_survived", nullable = false)
    private boolean isSurvived;

    @Column(nullable = false)
    private boolean activated;

    @Builder
    public QuizRecord(Long studyMemberId, QuizSet quizSet, boolean isSurvived, boolean activated) {
        this.studyMemberId = studyMemberId;
        this.quizSet = quizSet;
        this.isSurvived = isSurvived;
        this.activated = activated;
    }
}