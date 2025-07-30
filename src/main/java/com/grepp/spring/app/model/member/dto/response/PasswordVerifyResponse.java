package com.grepp.spring.app.model.member.dto.response;

import lombok.Getter;

@Getter
public class PasswordVerifyResponse {
    private final boolean matched;

    public PasswordVerifyResponse(boolean matched) {
        this.matched = matched;
    }
}
