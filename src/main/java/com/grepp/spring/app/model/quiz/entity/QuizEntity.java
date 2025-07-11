package com.grepp.spring.app.model.quiz.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quizSetId")
    private QuizSetEntity quizSet;

    @Column(nullable = true)
    private String question;

    @Column(nullable = true)
    private Integer answer;

    @NotNull
    @Column(nullable = false)
    private boolean activated;

    @OneToOne(mappedBy = "quiz", fetch = FetchType.LAZY)
    private ChoiceEntity choice;

    @Builder
    public QuizEntity(Long quizId, QuizSetEntity quizSet, String question, Integer answer, boolean activated, ChoiceEntity choice) {
        this.quizId = quizId;
        this.quizSet = quizSet;
        this.question = question;
        this.answer = answer;
        this.activated = activated;
        this.choice = choice;
    }
}