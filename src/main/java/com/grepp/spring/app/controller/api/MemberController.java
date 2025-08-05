package com.grepp.spring.app.controller.api;

import com.grepp.spring.app.model.member.dto.request.MemberUpdateRequest;
import com.grepp.spring.app.model.member.dto.request.PasswordVerifyRequest;
import com.grepp.spring.app.model.member.dto.response.AvatarInfoResponse;
import com.grepp.spring.app.model.member.dto.response.MemberInfoResponse;
import com.grepp.spring.app.model.member.dto.response.MemberMyPageResponse;
import com.grepp.spring.app.model.member.dto.response.MemberStudyListResponse;
import com.grepp.spring.app.model.member.dto.response.PasswordVerifyResponse;
import com.grepp.spring.app.model.member.dto.response.RequiredMemberInfoResponse;
import com.grepp.spring.app.model.member.service.MemberService;
import com.grepp.spring.app.model.reward.service.OwnItemService;
import com.grepp.spring.app.model.reward.service.RewardItemService;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.util.AuthorizationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 API", description = "회원 정보 조회 및 관리 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final RewardItemService rewardItemService;
    private final OwnItemService ownItemService;

    // 유저 정보 조회(이메일, 닉네임, 아바타)
    @Operation(
        summary = "간단한 회원 정보 조회",
        description = """
            회원 ID(`memberId`)를 이용하여 해당 회원의 이메일, 닉네임, 현재 장착한 아바타 이미지 주소를 조회합니다.
            """
    )
    @GetMapping("/{memberId}/info")
    public ResponseEntity<CommonResponse<MemberInfoResponse>> getMemberInfo(@PathVariable Long memberId) {

        MemberInfoResponse dto = memberService.getMemberInfo(memberId);

        return ResponseEntity.ok(CommonResponse.success(dto));
    }

    // 개인 정보 수정 (닉네임, 비밀번호 변경)
    @Operation(
        summary = "회원 정보 수정",
        description = """
    요청 body에 `MemberUpdateRequest`를 포함해야합니다.
    현재 로그인된 사용자의 정보를 수정합니다. 요청 본문을 통해 닉네임 또는 비밀번호를 변경할 수 있습니다.
    - 닉네임만 혹은 비밀번호만 변경을 원할 경우, 해당 필드만 요청을 보내면 됩니다.
    """
    )
    @PutMapping("/info")
    public ResponseEntity<CommonResponse<Void>> updateMemberInfo(
        @RequestBody MemberUpdateRequest request) {

        Long memberId = AuthorizationUtil.getCurrentMemberId();
        memberService.updateMemberInfo(memberId, request);
        return ResponseEntity.ok(CommonResponse.noContent());
    }

    // 기존 비밀번호 확인
    @Operation(
        summary = "기존 비밀번호 확인",
        description = """
    요청 body에 `PasswordVerifyRequest`를 포함해야 합니다.
    현재 로그인된 사용자의 비밀번호와 요청된 비밀번호를 비교하여 일치 여부를 확인합니다. 비밀번호 변경 전 본인 인증 용도로 사용됩니다."""
    )
    @PostMapping("/password/verify")
    public ResponseEntity<CommonResponse<PasswordVerifyResponse>> verifyPassword(
        @RequestBody PasswordVerifyRequest request) {

        Long memberId = AuthorizationUtil.getCurrentMemberId();
        boolean isMatched = memberService.verifyPassword(memberId, request.getPassword());
        var responseData = new PasswordVerifyResponse(isMatched);

        return ResponseEntity.ok(CommonResponse.success(responseData));
    }

    // 가입 스터디 조회
    @Operation(
        summary = "가입한 스터디 목록 조회",
        description = "회원 ID(`memberId`)를 이용하여 해당 회원이 가입한 모든 스터디의 목록을 조회합니다."
    )
    @GetMapping("/{memberId}/studies")
    public ResponseEntity<CommonResponse<MemberStudyListResponse>> getMemberStudyList(@PathVariable Long memberId) {

        MemberStudyListResponse dto = memberService.getMemberStudyList(memberId);

        return ResponseEntity.ok(CommonResponse.success(dto, "가입한 스터디 목록을 조회했습니다."));
    }

    // 유저 정보 요청(닉네임, 우승횟수, 스터디 수, 스터디 종류, 스터디별 출석률, 스터디별 목표달성률, 날짜별 일일 공부시간)
    @Operation(
        summary = "마이페이지 정보 조회",
        description = "회원 ID(`memberId`)를 이용하여 마이페이지에 필요한 모든 정보를 조회합니다. 닉네임, 우승 횟수, 스터디 통계, 공부 시간 등 복합적인 정보를 반환합니다."
    )
    @GetMapping("/{memberId}")
    public ResponseEntity<CommonResponse<MemberMyPageResponse>> getMemberMyPage(@PathVariable Long memberId) {

        MemberMyPageResponse dto = memberService.getMyPage(memberId);

        return ResponseEntity.ok(CommonResponse.success(dto, "마이페이지 정보를 성공적으로 불러왔습니다."));
    }

    // 요구사항 - 맴버테이블 전체 + 입고 있는 아바타 정보
    @Operation(
        summary = "필수 회원 정보 및 아바타 정보 조회",
        description = """
        로그인된 사용자의 필수 회원 정보와 현재 장착 중인 아바타 아이템 정보를 함께 조회합니다.
        - `memberInfo` 키로 회원의 필수 정보가, `avatarInfo` 키로 아바타 정보가 반환됩니다.
        - 이 API는 인증(로그인)이 필요하며, 요청 헤더에 유효한 토큰이 있어야 합니다.
        """
    )
    @GetMapping("/info-all")
    public ResponseEntity<CommonResponse<?>> infoAll() {
        Long memberId = AuthorizationUtil.getCurrentMemberId();
        RequiredMemberInfoResponse memberInfoRes =  memberService.getMemberRequiredInfo(memberId);
        AvatarInfoResponse avatarRes = memberService.getMemberAvatarInfo(memberId);
        LinkedHashMap<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("memberInfo", memberInfoRes);
        responseData.put("avatarInfo", avatarRes);
        return ResponseEntity.ok(CommonResponse.success(responseData));
    }
}