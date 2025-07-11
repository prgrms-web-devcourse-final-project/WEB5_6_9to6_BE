package com.grepp.spring.app.model.member.entity;

import com.grepp.spring.app.model.auth.code.Role;
import com.grepp.spring.app.model.member.code.Gender;
import com.grepp.spring.app.model.member.code.SocialType;
import com.grepp.spring.infra.entity.BaseEntity;
import com.grepp.spring.infra.error.exceptions.InsufficientRewardPointsException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer rewardPoints = 0;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer winRate = 0;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String avatarImage;

    @Builder
    public Member(Long id, String email, String password, String nickname, LocalDate birthday,
        Gender gender, Integer rewardPoints, Integer winRate, SocialType socialType, Role role,
        String avatarImage) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.rewardPoints = rewardPoints;
        this.winRate = winRate;
        this.socialType = socialType;
        this.role = role;
        this.avatarImage = avatarImage;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateSocialInfo(String nickname, LocalDate birthday, Gender gender) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void deductRewardPoints(int amount) {
        if (rewardPoints < amount) {
            throw new InsufficientRewardPointsException();
        }
        rewardPoints -= amount;
    }
}