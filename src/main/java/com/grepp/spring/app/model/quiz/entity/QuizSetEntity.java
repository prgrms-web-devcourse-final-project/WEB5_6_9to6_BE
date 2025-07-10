package com.grepp.spring.app.model.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz_set")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizSetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long quizSetId;

    @Column(nullable = false)
    private long studyId;

    @Column(nullable = false)
    private int week;

    @Column(nullable = false)
    private boolean activated;
}
