package com.grepp.spring.app.model.member;

import com.grepp.spring.app.model.auth.code.Role;
import com.grepp.spring.app.model.member.code.SocialType;
import com.grepp.spring.app.model.auth.dto.SignupRequest;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.infra.error.exceptions.AlreadyExistException;
import com.grepp.spring.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(SignupRequest req) {
        if (memberRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new AlreadyExistException(ResponseCode.ALREADY_EXIST);
        }

        Member member = Member.builder()
            .email(req.getEmail())
            .password(passwordEncoder.encode(req.getPassword()))
            .nickname(req.getNickname())
            .rewardPoints(100)
            .role(Role.ROLE_USER)
            .birthday(req.getBirthday())
            .gender(req.getGender())
            .socialType(SocialType.LOCAL)
            .winRate(0)
            .build();

        return memberRepository.save(member);
    }

    public boolean isDuplicatedEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }
}
