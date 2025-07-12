package com.grepp.spring.app.model.member.repository;

import com.grepp.spring.app.model.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
