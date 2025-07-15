package com.grepp.spring.app.model.member.dto.response;

import com.grepp.spring.app.model.auth.code.Role;
import com.grepp.spring.app.model.member.code.Gender;
import com.grepp.spring.app.model.member.code.SocialType;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequiredMemberInfoResponse {

    private Long id;
    private String email;
    private String nickname;
    private LocalDate birthday;
    private Gender gender;
    private Integer rewardPoints;
    private Integer winCount;
    private SocialType socialType;
    private Role role;

    @Builder
    public RequiredMemberInfoResponse(Long id, String email, String nickname,
        LocalDate birthday, Gender gender, Integer rewardPoints, Integer winCount,
        SocialType socialType, Role role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.rewardPoints = rewardPoints;
        this.winCount = winCount;
        this.socialType = socialType;
        this.role = role;
    }
}
