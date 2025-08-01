package com.grepp.spring.app.model.member.entity;

import com.grepp.spring.app.model.auth.code.Role;
import com.grepp.spring.app.model.member.code.Gender;
import com.grepp.spring.app.model.member.code.SocialType;
import com.grepp.spring.infra.entity.BaseEntity;
import com.grepp.spring.infra.error.exceptions.InsufficientRewardPointsException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    @Column
    private String nickname;

    @Column
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer rewardPoints = 0;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer winCount = 0;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String avatarImage;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<StudyMember> studyMembers = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String password, String nickname, LocalDate birthday,
        Gender gender, Integer rewardPoints, Integer winCount, SocialType socialType, Role role,
        String avatarImage) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.rewardPoints = rewardPoints;
        this.winCount = winCount;
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

    public void updateAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public void addRewardPoints(int amount) {
        this.rewardPoints += amount;
    }

    public void incrementWinCount() {
        this.winCount++;
    }
}