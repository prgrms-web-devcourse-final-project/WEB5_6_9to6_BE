package com.grepp.spring.app.model.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class VerifyCodeCheckRequest {
    @NotBlank(message = "이메일 입력이 누락되었습니다.")
    @Email
    private String email;

    @NotBlank(message = "6자리 코드값을 입력해주세요.")
    private String code;

}
