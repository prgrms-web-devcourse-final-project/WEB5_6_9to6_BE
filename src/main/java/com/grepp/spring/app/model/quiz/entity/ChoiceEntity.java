package com.grepp.spring.app.model.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "choice")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long choiceId;

    @Column(nullable = false)
    private String choice1;

    @Column(nullable = false)
    private String choice2;

    @Column(nullable = false)
    private String choice3;

    @Column(nullable = false)
    private String choice4;

    @Column(nullable = false)
    private Boolean activated;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quizId")
    private QuizEntity quiz;
}
