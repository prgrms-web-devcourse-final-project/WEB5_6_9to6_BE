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
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quizSetId")
    private QuizSet quizSet;

    @Column(nullable = true)
    private String question;

    @Column(nullable = true)
    private Integer answer;

    @NotNull
    @Column(nullable = false)
    private boolean activated;

    @OneToOne(mappedBy = "quiz", fetch = FetchType.LAZY)
    private Choice choice;

    @Builder
    public Quiz(QuizSet quizSet, String question, Integer answer, boolean activated, Choice choice) {
        this.quizSet = quizSet;
        this.question = question;
        this.answer = answer;
        this.activated = activated;
        this.choice = choice;
    }
}