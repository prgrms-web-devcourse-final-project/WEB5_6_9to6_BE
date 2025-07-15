package com.grepp.spring.app.controller.api.member;

import com.grepp.spring.app.controller.api.member.payload.request.MemberUpdateRequest;
import com.grepp.spring.app.controller.api.member.payload.request.PasswordVerifyRequest;
import com.grepp.spring.app.model.member.dto.response.MemberInfoResponse;
import com.grepp.spring.app.model.member.dto.response.MemberMyPageResponse;
import com.grepp.spring.app.model.member.dto.response.MemberStudyListResponse;
import com.grepp.spring.app.model.member.dto.response.PasswordVerifyResponse;
import com.grepp.spring.app.model.member.service.MemberService;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    // 유저 정보 조회(이메일, 닉네임, 아바타)
    @GetMapping("/{memberId}/info")
    public ResponseEntity<CommonResponse<MemberInfoResponse>> getMemberInfo(@PathVariable Long memberId) {

        MemberInfoResponse dto = memberService.getMemberInfo(memberId);

        return ResponseEntity.ok(CommonResponse.success(dto));
    }

    // 개인 정보 수정 (닉네임, 비밀번호 변경)
    @PutMapping("/{memberId}/info")
    public ResponseEntity<CommonResponse<Void>> updateMemberInfo(@PathVariable Long memberId,
        @RequestBody MemberUpdateRequest request) {

        memberService.updateMemberInfo(memberId, request);

        return ResponseEntity.ok(CommonResponse.noContent());
    }

    // 기존 비밀번호 확인
    @PostMapping("/{memberId}/password/verify")
    public ResponseEntity<CommonResponse<PasswordVerifyResponse>> verifyPassword(
        @PathVariable Long memberId,
        @RequestBody PasswordVerifyRequest request) {

        boolean isMatched = memberService.verifyPassword(memberId, request.getPassword());
        var responseData = new PasswordVerifyResponse(isMatched);

        return ResponseEntity.ok(CommonResponse.success(responseData));
    }

    // 가입 스터디 조회
    @GetMapping("/{memberId}/studies")
    public ResponseEntity<CommonResponse<MemberStudyListResponse>> getMemberStudyList(@PathVariable Long memberId) {

        MemberStudyListResponse dto = memberService.getMemberStudyList(memberId);

        return ResponseEntity.ok(CommonResponse.success(dto, "가입한 스터디 목록을 조회했습니다."));
    }

    // 유저 정보 요청(닉네임, 우승횟수, 스터디 수, 스터디 종류, 스터디별 출석률, 스터디별 목표달성률, 날짜별 일일 공부시간)
    @GetMapping("/{memberId}")
    public ResponseEntity<CommonResponse<MemberMyPageResponse>> getMemberMyPage(@PathVariable Long memberId) {

        MemberMyPageResponse dto = memberService.getMyPage(memberId);

        return ResponseEntity.ok(CommonResponse.success(dto, "마이페이지 정보를 성공적으로 불러왔습니다."));
    }

//    // 로그인 한 사용자 memberId
//    @GetMapping("/i-want-my-id")
//    public ResponseEntity<?> getMyId() {
//        return ResponseEntity.ok(CommonResponse.success(SecurityUtil.getCurrentMemberId()));
//    }
}