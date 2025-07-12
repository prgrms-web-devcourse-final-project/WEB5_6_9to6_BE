package com.grepp.spring.app.model.quiz.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private Long studyId;

    @Column(nullable = false)
    private Integer week;

    @Column(nullable = false)
    private boolean activated;

    @OneToMany(mappedBy = "quizSet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Quiz> quizzes;

    @Builder
    public QuizSet(Long quizSetId, Long studyId, Integer week, boolean activated, List<Quiz> quizzes) {
        this.Id = quizSetId;
        this.studyId = studyId;
        this.week = week;
        this.activated = activated;
        this.quizzes = quizzes;
    }
}