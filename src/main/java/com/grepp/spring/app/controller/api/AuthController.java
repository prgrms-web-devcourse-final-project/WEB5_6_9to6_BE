package com.grepp.spring.app.controller.api;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    // 이메일 인증 요청
    @PostMapping("/email/send")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> sendVerificationEmail(@RequestBody Map<String, Object> request) {

        String email = (String) request.get("email");

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return errorResponse("bad_request", "이메일 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("code", "0000");
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
        response.put("code", "0000");
        response.put("message", "인증 완료");
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // 이메일 중복 확인
    @GetMapping("/email/duplicate")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> checkEmailDuplicate(@RequestParam String email) {

        Map<String, Object> data = new HashMap<>();
        data.put("available", true);

        Map<String, Object> response = new HashMap<>();
        response.put("code", "0000");
        response.put("message", "사용 가능 이메일");
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // 비밀번호 유효성 검사
    @PostMapping("/password/verify")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> validatePassword(@RequestBody Map<String, String> request) {

        String password = request.get("password");

        Map<String, Object> data = new HashMap<>();
        data.put("valid", true);

        Map<String, Object> response = new HashMap<>();
        response.put("code", "0000");
        response.put("message", "유효한 비밀번호입니다.");
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // 회원가입
    @PostMapping("/signup")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody Map<String, Object> request) {

        String email = (String) request.get("email");
        String password = (String) request.get("password");
        String nickname = (String) request.get("nickname");
        String birthDate = (String) request.get("birth_date");
        String gender = (String) request.get("gender");

        // 이메일 필수 + 형식 검사
        if (email == null || email.isBlank()) {
            return errorResponse("bad_request", "이메일을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return errorResponse("bad_request", "이메일 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        // 나머지 값 필수
        if (password == null || password.isBlank()) {
            return errorResponse("bad_request", "비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        if (nickname == null || nickname.isBlank()) {
            return errorResponse("bad_request", "닉네임을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        if (birthDate == null || birthDate.isBlank()) {
            return errorResponse("bad_request", "생년월일을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        if (gender == null || gender.isBlank()) {
            return errorResponse("bad_request", "성별을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("memberId", 123);

        Map<String, Object> response = new HashMap<>();
        response.put("code", "0000");
        response.put("message", "회원가입 완료.");
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // 로그인
    @PostMapping("/login")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> request) {
        String email = (String) request.get("email");
        String password = (String) request.get("password");

        // 이메일 필수
        if (email == null || email.isBlank() || email.equalsIgnoreCase("null")) {
            return errorResponse("bad_request", "이메일을 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        // 이메일 형식 검증
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return errorResponse("bad_request", "이메일 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        // 비밀번호 필수
        if (password == null || password.isBlank()) {
            return errorResponse("bad_request", "비밀번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("memberId", 123);
        data.put("accessToken", "access-token-value");
        data.put("refreshToken", "refresh-token-value");

        Map<String, Object> response = new HashMap<>();
        response.put("code", "0000");
        response.put("message", "로그인 성공");
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // 공통 에러 응답 메서드
    private ResponseEntity<Map<String, Object>> errorResponse(String code, String message, HttpStatus status) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", code);
        error.put("message", message);
        return ResponseEntity.status(status).body(error);
    }

}



