package com.grepp.spring.app.model.study.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoalsResponse {

    private Long id;
    private String content;

    public GoalsResponse(Long id, String content) {
        this.content = content;
        this.id = id;
    }
}
