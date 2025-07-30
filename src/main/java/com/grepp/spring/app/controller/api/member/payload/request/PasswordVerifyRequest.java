package com.grepp.spring.app.controller.api.member.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordVerifyRequest {

    private String password;

}
