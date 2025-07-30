package com.grepp.spring.app.controller.api.member.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequest {

    private String nickname;

    private String currentPassword;
    private String newPassword;
}
