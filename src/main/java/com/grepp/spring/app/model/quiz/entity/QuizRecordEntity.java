package com.grepp.spring.app.model.quiz.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizRecordId;

    @NotNull
    @Column(nullable = false)
    private Long studyMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quizSetId")
    private QuizSetEntity quizSet;

    @NotNull
    @Column(nullable = false)
    private boolean isPassed;

    @NotNull
    @Column(nullable = false)
    private boolean activated;

    @Builder
    public QuizRecordEntity(Long quizRecordId, Long studyMemberId, QuizSetEntity quizSet, boolean isPassed, boolean activated) {
        this.quizRecordId = quizRecordId;
        this.studyMemberId = studyMemberId;
        this.quizSet = quizSet;
        this.isPassed = isPassed;
        this.activated = activated;
    }
}