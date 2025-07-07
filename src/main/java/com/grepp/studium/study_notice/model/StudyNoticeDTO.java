package com.grepp.studium.study_notice.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StudyNoticeDTO {

    private Integer noticeId;

    @NotNull
    @Size(max = 255)
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @NotNull
    private Boolean activated;

    @NotNull
    @StudyNoticeStudyUnique
    private Integer study;

}
