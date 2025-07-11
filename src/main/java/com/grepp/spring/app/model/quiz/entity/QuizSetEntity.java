package com.grepp.spring.app.model.quiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz_set")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizSetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quizSetId;

    @Column(nullable = false)
    private Long studyId;

    @Column(nullable = false)
    private Integer week;

    @Column(nullable = false)
    private Boolean activated;

    @OneToMany(mappedBy = "quizSet", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<QuizEntity> quizzes = new ArrayList<>();
}
