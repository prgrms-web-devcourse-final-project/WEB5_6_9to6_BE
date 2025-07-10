package com.grepp.spring.app.model.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoResponse {

    private String email;
    private String nickname;
    private String avatarImage;

    @Builder
    public MemberInfoResponse(String email, String nickname, String avatarImage) {
        this.email = email;
        this.nickname = nickname;
        this.avatarImage = avatarImage;
    }
}
