package com.grepp.spring.app.model.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@AllArgsConstructor
public class EmailDuplicatedCheckRequest {

    @NotBlank(message = "빈 값입니다.")
    @Email
    private String email;

}
