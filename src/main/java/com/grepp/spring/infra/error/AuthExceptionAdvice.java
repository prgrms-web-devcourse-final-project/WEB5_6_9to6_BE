package com.grepp.spring.infra.error;

import com.grepp.spring.infra.error.exceptions.AuthApiException;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class AuthExceptionAdvice {
    
    @ResponseBody
    @ExceptionHandler(AuthApiException.class)
    public ResponseEntity<CommonResponse<String>> authApiExHandler(
        AuthApiException ex) {
        return ResponseEntity
                   .status(ex.code().status())
                   .body(CommonResponse.error(ex.code()));
    }
    
    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonResponse<String>> authExHandler(
        AuthenticationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                   .status(HttpStatus.UNAUTHORIZED)
                   .body(CommonResponse.error(ResponseCode.UNAUTHORIZED));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonResponse<String>> authExHandler(
        AccessDeniedException ex
    ) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(CommonResponse.error(ResponseCode.UNAUTHORIZED));
    }
}
