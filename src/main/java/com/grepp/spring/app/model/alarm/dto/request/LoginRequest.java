package com.grepp.spring.app.model.alarm.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
