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
    private Long quizRecordId;

    @Column(nullable = false)
    private Long studyMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quizSetId")
    private QuizSetEntity quizSet;

    @Column(nullable = false)
    private Boolean isPassed;

    @Column(nullable = false)
    private Boolean activated;
}
