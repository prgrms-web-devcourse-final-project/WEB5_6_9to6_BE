package com.grepp.spring.infra.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "공통 응답 DTO", name = "ApiResponse")
public record CommonResponse<T>(
    @Schema(description = "서비스 정의 코드", example = "2000")
    String code,
    @Schema(description = "응답 메시지", example = "성공적으로 처리되었습니다.")
    String message,
    @Schema(description = "응답 데이터")
    T data
) {
    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.message(), data);
    }

    public static <T> CommonResponse<T> success(T data, String message) {
        return new CommonResponse<>(ResponseCode.SUCCESS.code(), message, data);
    }

    public static <T> CommonResponse<T> noContent() {
        return new CommonResponse<>(ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.message(), null);
    }

    public static <T> CommonResponse<T> noContent(SuccessCode message) {
        return new CommonResponse<>(message.code(), message.message(), null);
    }

    public static <T> CommonResponse<T> error(ResponseCode code) {
        return new CommonResponse<>(code.code(), code.message(), null);
    }

    public static <T> CommonResponse<T> error(ResponseCode code, T data) {
        return new CommonResponse<>(code.code(), code.message(), data);
    }

    public static <T> CommonResponse<T> error(String code, String message) {
        return new CommonResponse<>(code, message, null);
    }
}
