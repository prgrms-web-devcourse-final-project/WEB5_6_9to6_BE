package com.grepp.spring.app.model.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Column(nullable = false)
    private Boolean activated;

    @OneToOne(mappedBy = "quiz", fetch = FetchType.LAZY)
    private ChoiceEntity choice;
}