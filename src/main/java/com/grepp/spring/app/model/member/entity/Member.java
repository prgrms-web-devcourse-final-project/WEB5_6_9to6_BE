
package com.grepp.spring.app.model.member.entity;

import com.grepp.spring.app.model.auth.code.Role;
import com.grepp.spring.app.model.member.code.Gender;
import com.grepp.spring.app.model.member.code.SocialType;
import com.grepp.spring.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private Role role;
  
    @Builder
    public Member(long id, String email, String password, String nickname, int rewardPoints,
        Role role, SocialType socialType, LocalDate birthday, Gender gender, int winRate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.rewardPoints = rewardPoints;
        this.role = role;
        this.socialType = socialType;
        this.birthday = birthday;
        this.gender = gender;
        this.winRate = winRate;
    }
  
    public void updateSocialInfo(String nickname, LocalDate birthday, Gender gender) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void deductRewardPoints(int amount) {
        if (rewardPoints < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        rewardPoints -= amount;
    }


}

}

}

    public void updateSocialInfo(String nickname, LocalDate birthday, Gender gender) {
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
    }

    public void deductRewardPoints(int amount) {
        if (rewardPoints < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        rewardPoints -= amount;
    }


}


