// Member.java
package com.grepp.spring.app.model.member.entity;

import com.grepp.spring.app.model.auth.code.Role;
import com.grepp.spring.app.model.member.code.Gender;
import com.grepp.spring.app.model.member.code.SocialType;
import com.grepp.spring.infra.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String email;
    String password;
    String nickname;
    int rewardPoints;
    @Enumerated(EnumType.STRING)
    Role role;
    @Enumerated(EnumType.STRING)
    SocialType socialType;
    LocalDate birthday;
    @Enumerated(EnumType.STRING)
    Gender gender;
    int winRate;

    @Builder
    public Member(long id, String email, String password, String nickname, int rewardPoints,
        Role role, SocialType socialType, LocalDate birthday, Gender gender, int winRate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname; // s
        this.rewardPoints = rewardPoints;
        this.role = role;
        this.socialType = socialType;
        this.birthday = birthday; // s
        this.gender = gender; // s
        this.winRate = winRate;
    }

    public void updateSocialInfo(String nickname, LocalDate birthday, Gender gender) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
    }

}