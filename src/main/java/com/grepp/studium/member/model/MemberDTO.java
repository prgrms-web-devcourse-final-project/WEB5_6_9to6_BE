package com.grepp.studium.member.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MemberDTO {

    private Integer memberId;

    @NotNull
    @Size(max = 255)
    @MemberEmailUnique
    private String email;

    @Size(max = 255)
    private String password;

    @NotNull
    @Size(max = 255)
    private String nickname;

    @Size(max = 255)
    private String profileImage;

    @NotNull
    private Integer rewardPoint;

    @NotNull
    @Size(max = 255)
    private String role;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    @Size(max = 255)
    private String socialType;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    @Size(max = 255)
    private String gender;

    @NotNull
    private Boolean activated;

    @NotNull
    private Integer winRate;

}
