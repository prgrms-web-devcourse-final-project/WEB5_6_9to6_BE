package com.grepp.spring.app.controller.api.auth;

import com.grepp.spring.app.controller.api.auth.payload.LoginRequest;
import com.grepp.spring.app.controller.api.auth.payload.TokenResponse;
import com.grepp.spring.app.model.auth.AuthService;
import com.grepp.spring.app.model.auth.code.AuthToken;
import com.grepp.spring.app.model.auth.dto.EmailDuplicatedCheckResponse;
import com.grepp.spring.app.model.auth.dto.SignupRequest;
import com.grepp.spring.app.model.auth.dto.TokenDto;
import com.grepp.spring.app.model.member.MemberService;
import com.grepp.spring.infra.auth.jwt.TokenCookieFactory;
import com.grepp.spring.infra.response.CommonResponse;
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
    public ResponseEntity<?> signup(
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
    public ResponseEntity<Map<String, Object>> sendVerificationEmail(@RequestBody Map<String, Object> request) {

        String email = (String) request.get("email");

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return errorResponse("bad_request", "이메일 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "인증 코드 전송.");

        return ResponseEntity.ok(response);
    }

    // 이메일 인증 확인
    @PostMapping("/email/verify")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> verifyEmailCode(@RequestBody Map<String, Object> request) {

        String email = (String) request.get("email");
        String code = (String) request.get("code");

        // 인증 코드 누락
        if (code == null || code.isBlank()) {
            return errorResponse("bad_request", "인증 코드를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        // 인증 코드가 틀리거나 만료됨
        if (!"ABC12".equals(code)) {
            return errorResponse("unauthorized", "인증 코드가 틀렸거나 만료되었습니다.", HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("verified", true);

        Map<String, Object> response = new HashMap<>();
        response.put("code", "SUCCESS");
        response.put("message", "인증 완료");
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // 이메일 중복 확인
    @GetMapping("/email/duplicate")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<CommonResponse> checkEmailDuplicate(@RequestParam String email) {
        EmailDuplicatedCheckResponse duplicated
            = new EmailDuplicatedCheckResponse(memberService.isDuplicatedEmail(email));
        return ResponseEntity.ok(CommonResponse.success(duplicated));
    }

    // 비밀번호 유효성 검사
//    @PostMapping("/password/verify")
//    @ApiResponse(responseCode = "200")
//    public ResponseEntity<Map<String, Object>> validatePassword(@RequestBody Map<String, String> request) {
//
//        String password = request.get("password");
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("valid", true);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("code", "SUCCESS");
//        response.put("message", "유효한 비밀번호입니다.");
//        response.put("data", data);
//
//        return ResponseEntity.ok(response);
//    }

    // 공통 에러 응답 메서드
    private ResponseEntity<Map<String, Object>> errorResponse(String code, String message, HttpStatus status) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", code);
        error.put("message", message);
        return ResponseEntity.status(status).body(error);
    }

}



