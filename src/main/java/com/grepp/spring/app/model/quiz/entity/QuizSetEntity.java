package com.grepp.spring.app.model.quiz.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "quiz_set")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizSetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizSetId;

    @NotNull
    @Column(nullable = false)
    private Long studyId;

    @NotNull
    @Column(nullable = false)
    private Integer week;

    @NotNull
    @Column(nullable = false)
    private boolean activated;

    @OneToMany(mappedBy = "quizSet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<QuizEntity> quizzes;

    @Builder
    public QuizSetEntity(Long quizSetId, Long studyId, Integer week, boolean activated, List<QuizEntity> quizzes) {
        this.quizSetId = quizSetId;
        this.studyId = studyId;
        this.week = week;
        this.activated = activated;
        this.quizzes = quizzes;
    }
}