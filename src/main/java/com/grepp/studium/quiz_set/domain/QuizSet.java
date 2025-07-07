package com.grepp.studium.quiz_set.domain;

import com.grepp.studium.quiz.domain.Quiz;
import com.grepp.studium.quiz_record.domain.QuizRecord;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "QuizSets")
@Getter
@Setter
public class QuizSet {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer quizSetId;

    @Column(nullable = false)
    private Integer week;

    @Column(nullable = false)
    private Boolean activated;

    @OneToMany(mappedBy = "quizSet")
    private Set<QuizRecord> quizSetQuizRecords = new HashSet<>();

    @OneToMany(mappedBy = "quizSet")
    private Set<Quiz> quizSetQuizzes = new HashSet<>();

}
