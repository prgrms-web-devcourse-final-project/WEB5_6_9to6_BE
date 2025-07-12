package com.grepp.spring.app.model.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Choice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String choice1;

    @Column(nullable = false)
    private String choice2;

    @Column(nullable = false)
    private String choice3;

    @Column(nullable = false)
    private String choice4;

    @Column(nullable = false)
    private boolean activated;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quizId")
    private Quiz quiz;

    @Builder
    public Choice(Long choiceId, String choice1, String choice2, String choice3, String choice4, boolean activated, Quiz quiz) {
        this.Id = choiceId;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.activated = activated;
        this.quiz = quiz;
    }
}