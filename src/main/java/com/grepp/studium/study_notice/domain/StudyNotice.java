package com.grepp.studium.study_notice.domain;

import com.grepp.studium.study.domain.Study;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "StudyNotices")
@Getter
@Setter
public class StudyNotice {

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
    private Integer noticeId;

    @Column(nullable = false)
    private String content;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private Boolean activated;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false, unique = true)
    private Study study;

}
