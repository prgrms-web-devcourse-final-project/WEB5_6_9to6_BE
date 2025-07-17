package com.grepp.spring.app.model.member.repository;

import com.grepp.spring.app.model.member.dto.response.RequiredMemberInfoResponse;
import com.grepp.spring.app.model.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("SELECT m.id FROM Member m WHERE m.email = :email")
    Long findIdByEmail(@Param("email") String email);


    @Query("SELECT m.nickname FROM Member m WHERE m.id = :id")
    String findNicknameById(@Param("id")Long Id);

    @Query("select new com.grepp.spring.app.model.member.dto.response.RequiredMemberInfoResponse"
        + "(m.id, m.email, m.nickname, m.birthday, m.gender, m.rewardPoints, m.winCount, m.socialType, m.role) "
        + "from Member m where m.activated = true and m.id = :memberId")
    RequiredMemberInfoResponse findRequiredMemberInfo(@Param("memberId") Long memberId);

    @Query("select m.avatarImage  from Member m where m.id = :memberId")
    String findAvatarImageById(@Param("memberId") Long memberId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Member m SET m.rewardPoints = m.rewardPoints + :points WHERE m.id = :memberId")
    void addRewardPoints(@Param("memberId") Long memberId, @Param("points") int points);
}
