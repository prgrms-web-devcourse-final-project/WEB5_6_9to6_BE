package com.grepp.spring.app.controller.api.auth;

import com.grepp.spring.app.controller.api.auth.payload.LoginRequest;
import com.grepp.spring.app.controller.api.auth.payload.TokenResponse;
import com.grepp.spring.app.model.auth.AuthService;
import com.grepp.spring.app.model.auth.code.AuthToken;
import com.grepp.spring.app.model.auth.dto.EmailDuplicatedCheckResponse;
import com.grepp.spring.app.model.auth.dto.SendEmailRequest;
import com.grepp.spring.app.model.auth.dto.SignupRequest;
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
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
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
    public ResponseEntity<CommonResponse<SuccessCode>> sendVerificationEmail(@Valid @RequestBody SendEmailRequest req) {
//        authService.sendVerifyCode(req.getEmail());
        return ResponseEntity.ok(CommonResponse.noContent(SuccessCode.SEND_MAIL));
    }

    // 이메일 인증 확인
    @PostMapping("/email/verify")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> verifyEmailCode(@Valid @RequestBody VerifyCodeCheckRequest req) {
//        VerifyCodeCheckResponse check
//            = new VerifyCodeCheckResponse(authService.checkVerifyCode(req.getEmail(), req.getCode()));
        boolean mock = false;
        if(req.getCode().equals("123456")) mock = true;
        VerifyCodeCheckResponse check = new VerifyCodeCheckResponse(mock);
        return ResponseEntity.ok(CommonResponse.success(check));
    }

    // 이메일 중복 확인
    @GetMapping("/email/duplicate")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse<EmailDuplicatedCheckResponse>> checkEmailDuplicate(@RequestParam String email) {
        EmailDuplicatedCheckResponse duplicated
            = new EmailDuplicatedCheckResponse(memberService.isDuplicatedEmail(email));
        return ResponseEntity.ok(CommonResponse.success(duplicated));
    }

    // 공통 에러 응답 메서드
    private ResponseEntity<Map<String, Object>> errorResponse(String code, String message, HttpStatus status) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", code);
        error.put("message", message);
        return ResponseEntity.status(status).body(error);
    }

    // 예외 핸들러 - 클래스 범위
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", "4000");
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}



