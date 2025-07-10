package com.grepp.spring.app.controller.api.auth;

import com.grepp.spring.app.controller.api.auth.payload.LoginRequest;
import com.grepp.spring.app.controller.api.auth.payload.TokenResponse;
import com.grepp.spring.app.model.auth.AuthService;
import com.grepp.spring.app.model.auth.code.AuthToken;
import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.app.model.auth.dto.EmailDuplicatedCheckRequest;
import com.grepp.spring.app.model.auth.dto.EmailDuplicatedCheckResponse;
import com.grepp.spring.app.model.auth.dto.EmailSendRequest;
import com.grepp.spring.app.model.auth.dto.SignupRequest;
import com.grepp.spring.app.model.auth.dto.SocialMemberInfoRegistRequest;
import com.grepp.spring.app.model.auth.dto.TokenDto;
import com.grepp.spring.app.model.auth.dto.VerifyCodeCheckRequest;
import com.grepp.spring.app.model.auth.dto.VerifyCodeCheckResponse;
import com.grepp.spring.app.model.member.MemberService;
import com.grepp.spring.infra.auth.jwt.TokenCookieFactory;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.ResponseCode;
import com.grepp.spring.infra.response.SuccessCode;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<ResponseCode>> signup(
        @Valid @RequestBody SignupRequest req) {
        memberService.join(req);
        return ResponseEntity.ok(CommonResponse.noContent());
    }

    // 로그인
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
    @PostMapping("/email/send")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<SuccessCode>> sendVerificationEmail(@Valid @RequestBody EmailSendRequest req) {
        authService.sendVerifyCode(req.getEmail());
        return ResponseEntity.ok(CommonResponse.noContent(SuccessCode.SEND_MAIL));
    }

    // 이메일 인증 확인
    @PostMapping("/email/verify")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> verifyEmailCode(@Valid @RequestBody VerifyCodeCheckRequest req) {
        VerifyCodeCheckResponse verified
            = new VerifyCodeCheckResponse(authService.checkVerifyCode(req.getEmail(), req.getCode()));
        return ResponseEntity.ok(CommonResponse.success(verified));
    }

    // 이메일 중복 확인
    @PostMapping("/email/duplicate")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<EmailDuplicatedCheckResponse>> checkEmailDuplicate(@RequestBody EmailDuplicatedCheckRequest req) {
        EmailDuplicatedCheckResponse duplicated
            = new EmailDuplicatedCheckResponse(memberService.isDuplicatedEmail(req.getEmail()));
        return ResponseEntity.ok(CommonResponse.success(duplicated));
    }

    // 첫 소셜 로그인 유저 추가 정보 입력
    @PutMapping("/oauth/first-regist")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<?>> oauthRegistMember(
        @Valid @RequestBody SocialMemberInfoRegistRequest req,
        Authentication authentication
    ) {
        Principal principal = (Principal) authentication.getPrincipal();
        long memberId = principal.getMemberId();
        log.info("memberId: {}", memberId);
        memberService.updateMemberInfoById(memberId, req);
        return ResponseEntity.ok(CommonResponse.noContent());
    }

}



