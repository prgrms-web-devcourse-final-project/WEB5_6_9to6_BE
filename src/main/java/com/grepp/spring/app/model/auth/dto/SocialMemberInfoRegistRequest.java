package com.grepp.spring.app.model.auth.dto;

import com.grepp.spring.app.model.member.code.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class SocialMemberInfoRegistRequest {

    @NotBlank(message = "닉네임이 비어있습니다.")
    private String nickname;

    @NotNull(message = "생일이 비어있습니다.")
    private LocalDate birthday;

    @NotNull(message = "성별이 비어있습니다.")
    private Gender gender;

    public SocialMemberInfoRegistRequest(String nickname, LocalDate birthday, Gender gender) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
    }
}
