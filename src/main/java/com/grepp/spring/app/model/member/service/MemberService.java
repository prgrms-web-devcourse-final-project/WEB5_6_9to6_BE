package com.grepp.spring.app.model.member.service;

import com.grepp.spring.app.model.auth.code.Role;
import com.grepp.spring.app.model.auth.dto.SignupRequest;
import com.grepp.spring.app.model.auth.dto.SocialMemberInfoRegistRequest;
import com.grepp.spring.app.model.member.code.SocialType;
import com.grepp.spring.app.controller.api.member.payload.request.MemberUpdateRequest;
import com.grepp.spring.app.model.member.dto.response.MemberInfoResponse;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.infra.error.exceptions.AlreadyExistException;
import com.grepp.spring.infra.error.exceptions.NotFoundException;
import com.grepp.spring.infra.response.ResponseCode;
import jakarta.persistence.EntityNotFoundException;
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
            .avatarImage(null)
            .build();

        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicatedEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void updateMemberInfoById(long memberId , SocialMemberInfoRegistRequest req) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND.message()));

        member.updateSocialInfo(
            req.getNickname(),
            req.getBirthday(),
            req.getGender()
        );
    }

    // 개인 정보 조회(이메일, 닉네임, 아바타)
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        return MemberInfoResponse.builder()
            .email(member.getEmail())
            .nickname(member.getNickname())
            .avatarImage(member.getAvatarImage())
            .build();
    }

    // 개인 정보 수정
    @Transactional
    public void updateMemberInfo(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 회원을 찾을 수 없습니다"));

        // 닉네임 변경
        if (request.getNickname() != null && !request.getNickname().isBlank()) {
            member.updateNickname(request.getNickname());
        }

        // 비밀번호 변경
        if (request.getCurrentPassword() != null && request.getNewPassword() != null && request.getNewPasswordCheck() != null)  {

            // 기존 비밀번호 일치 확인
            if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
            }

            // 새 비밀번호 확인
            if (!request.getNewPassword().equals(request.getNewPasswordCheck())) {
                throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
            }

            member.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        }
    }

    // 기존 비밀번호 일치 여부
    @Transactional(readOnly = true)
    public boolean verifyPassword(Long memberId, String inputPassword) {
        // 1. memberId로 회원을 조회하고, 없으면 예외를 발생시킵니다.
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("해당 ID의 회원을 찾을 수 없습니다"));

        // 2. 입력된 비밀번호(평문)와 DB에 저장된 암호화된 비밀번호를 비교합니다.
        // passwordEncoder.matches(평문, 암호화된_문자열)
        return passwordEncoder.matches(inputPassword, member.getPassword());
    }
}
