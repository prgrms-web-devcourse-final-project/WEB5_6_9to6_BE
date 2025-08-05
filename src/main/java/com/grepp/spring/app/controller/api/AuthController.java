package com.grepp.spring.app.controller.api;

import com.grepp.spring.app.model.alarm.dto.request.LoginRequest;
import com.grepp.spring.app.model.alarm.dto.response.TokenResponse;
import com.grepp.spring.app.model.auth.AuthService;
import com.grepp.spring.app.model.auth.code.AuthToken;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.auth.dto.EmailSendRequest;
import com.grepp.spring.app.model.auth.dto.SignupRequest;
import com.grepp.spring.app.model.auth.dto.SocialMemberInfoRegistRequest;
import com.grepp.spring.app.model.auth.dto.TokenDto;
import com.grepp.spring.app.model.auth.dto.VerifyCodeCheckRequest;
import com.grepp.spring.app.model.auth.dto.VerifyCodeCheckResponse;
import com.grepp.spring.app.model.member.service.MemberService;
import com.grepp.spring.infra.auth.jwt.TokenCookieFactory;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.ResponseCode;
import com.grepp.spring.infra.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "인증 API", description = "사용자 회원가입, 로그인 등 인증 관련 API 입니다.")
@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    // 회원가입
    @Operation(summary = "일반 회원가입",
        description = """
            요청 body에 `SignupRequest`를 포함하여야 합니다.
            이메일, 비밀번호, 닉네임 등 사용자 정보를 받아 일반 회원가입을 처리합니다.
            """)
    @PostMapping("/signup")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<ResponseCode>> signup(
        @Valid @RequestBody SignupRequest req) {
        memberService.join(req);
        return ResponseEntity.ok(CommonResponse.noContent());
    }

    // 로그인
    @Operation(summary = "로그인", description = """
        이메일과 비밀번호로 로그인을 처리하고, 성공 시 **Access Token**과 **Refresh Token**을 발급합니다.
        - 발급된 토큰은 응답 본문과 함께 **쿠키**로도 설정되어 클라이언트에 전달됩니다.
        """)
    @PostMapping("/login")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<TokenResponse>> login(
        @RequestBody LoginRequest loginRequest,
        HttpServletResponse response
    ) {
        TokenDto tokenDto = authService.signin(loginRequest);

        ResponseCookie accessToken = TokenCookieFactory.create(AuthToken.ACCESS_TOKEN.name(),
            tokenDto.getAccessToken(), tokenDto.getExpiresIn());
        ResponseCookie refreshToken = TokenCookieFactory.create(AuthToken.REFRESH_TOKEN.name(),
            tokenDto.getRefreshToken(), tokenDto.getExpiresIn());

        response.addHeader("Set-Cookie", accessToken.toString());
        response.addHeader("Set-Cookie", refreshToken.toString());

        return ResponseEntity.ok(CommonResponse.success(
            TokenResponse.builder().
                accessToken(tokenDto.getAccessToken())
                .grantType(tokenDto.getGrantType())
                .expiresIn(tokenDto.getExpiresIn())
                .build())
        );
    }

    // 이메일 인증 요청
    @Operation(summary = "이메일 인증 코드 발송",
        description = """
            요청 body에 `EmailSendRequest`를 포함하여야 합니다.
            회원가입을 위해 입력된 이메일 주소로 6자리 인증 코드를 발송합니다.
            만약 이미 이메일이 DB에 존재한다면 중복된 이메일에 대한 예외 메시지가 전송됩니다.
            """
    )
    @PostMapping("/email/send")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<SuccessCode>> sendVerificationEmail(@Valid @RequestBody EmailSendRequest req) {
        authService.sendVerifyCode(req.getEmail());
        return ResponseEntity.ok(CommonResponse.noContent(SuccessCode.SEND_MAIL));
    }

    // 이메일 인증 확인
    @Operation(summary = "이메일 인증 코드 확인",
        description = """
            요청 body에 `VerifyCodeCheckRequest`를 포함하여야 합니다.
            사용자가 입력한 이메일과 인증 코드가 유효한지 확인합니다. 성공 시 "data":{verified: true}를 반환합니다.)
            """
    )
    @PostMapping("/email/verify")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> verifyEmailCode(@Valid @RequestBody VerifyCodeCheckRequest req) {
        VerifyCodeCheckResponse verified
            = new VerifyCodeCheckResponse(authService.checkVerifyCode(req.getEmail(), req.getCode()));
        return ResponseEntity.ok(CommonResponse.success(verified));
    }

    @Operation(summary = "소셜 로그인 추가 정보 입력", description = """
        요청 body에 `SocialMemberInfoRegistRequest`를 포함하여야 합니다.
        소셜 로그인으로 최초 가입한 사용자가 필수 추가 정보(닉네임, 생년월일 등)를 입력하고 최종 회원가입을 완료합니다.
        - 이 API는 소셜 로그인 직후, 추가 정보가 필요한 사용자만 호출합니다.
        - 요청 시 헤더에 유효한 **Access Token**이 반드시 포함되어야 합니다.
        """)
    @PutMapping("/oauth/first-regist")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<?>> oauthRegistMember(
        @Valid @RequestBody SocialMemberInfoRegistRequest req,
        Authentication authentication
    ) {
        Principal principal = (Principal) authentication.getPrincipal();
        Long memberId = principal.getMemberId();
        log.info("memberId: {}", memberId);
        memberService.updateMemberInfoById(memberId, req);
        return ResponseEntity.ok(CommonResponse.noContent());
    }

    @Operation(summary = "Access Token 정보 조회",
        description = """
            현재 로그인한 사용자의 Access Token 정보를 조회합니다.
            - 요청 시 브라우저에 저장된 **Access Token 쿠키**가 자동으로 사용됩니다.
            - **참고:** 이 API는 토큰 문자열 자체를 반환하며, 사용자 ID나 이메일 등은 포함하지 않습니다.
            """
    )
    @GetMapping("/oauth/get-token")
    public ResponseEntity<CommonResponse<TokenResponse>> getToken(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Principal principal = (Principal) authentication.getPrincipal();

        String accessToken = principal.getAccessToken()
            .orElse(null);

        TokenResponse tokenResponse = TokenResponse.builder()
            .accessToken(accessToken)
            .grantType("Bearer")
            .expiresIn(null)
            .build();

        return ResponseEntity.ok(CommonResponse.success(tokenResponse));
    }

}