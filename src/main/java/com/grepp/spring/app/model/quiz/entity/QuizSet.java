package com.grepp.spring.app.model.quiz.entity;

import com.grepp.spring.app.model.study.entity.StudyGoal;
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
    private Long id;

    @Column(nullable = false)
    private Long studyId;

    @Column(nullable = false)
    private Integer week;

    @Column(nullable = false)
    private boolean activated;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", unique = true)
    private StudyGoal studyGoal;

    @OneToMany(mappedBy = "quizSet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Quiz> quizzes;

    @Builder
    public QuizSet(Long studyId, Integer week, boolean activated, StudyGoal studyGoal, List<Quiz> quizzes) {
        this.studyId = studyId;
        this.week = week;
        this.activated = activated;
        this.studyGoal = studyGoal;
        this.quizzes = quizzes;
    }
}