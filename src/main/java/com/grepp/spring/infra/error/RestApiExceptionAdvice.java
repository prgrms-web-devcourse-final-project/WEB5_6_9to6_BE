package com.grepp.spring.infra.error;

import com.grepp.spring.infra.error.exceptions.AlreadyExistException;
import com.grepp.spring.infra.error.exceptions.CommonException;
import com.grepp.spring.infra.error.exceptions.MailSendFailureException;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.ResponseCode;
import com.grepp.spring.infra.util.NotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.grepp.spring.app.controller.api")
@Slf4j
public class RestApiExceptionAdvice {

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<CommonResponse<Map<String, String>>>
//    validatorHandler(MethodArgumentNotValidException ex) {
//        log.error(ex.getMessage(), ex);
//        Map<String, String> errors = new LinkedHashMap<>();
//        ex.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
//        return ResponseEntity
//                   .badRequest()
//                   .body(CommonResponse.error(ResponseCode.BAD_REQUEST, errors));
//    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonResponse<String>>
    methodNotSupportedHandler(HttpRequestMethodNotSupportedException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                   .badRequest()
                   .body(CommonResponse.error(ResponseCode.BAD_REQUEST, ex.getMessage()));
    }
    
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<CommonResponse<String>> restApiExceptionHandler(CommonException ex) {
        return ResponseEntity
                   .status(ex.code().status())
                   .body(CommonResponse.error(ex.code()));
    }
    
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<CommonResponse<String>>  authorizationDeniedHandler(AuthorizationDeniedException ex, Model model){
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                   .status(HttpStatus.UNAUTHORIZED)
                   .body(CommonResponse.error(ResponseCode.UNAUTHORIZED));
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse<String>> runtimeExceptionHandler(RuntimeException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
                   .internalServerError()
                   .body(CommonResponse.error(ResponseCode.INTERNAL_SERVER_ERROR));
    }

//    @ExceptionHandler(AlreadyExistException.class)
//    public ResponseEntity<CommonResponse<String>> alreadyExistExceptionHandler(AlreadyExistException ex) {
//        log.error(ex.getMessage(), ex);
//        ResponseCode code = ResponseCode.ALREADY_EXIST;
//        return ResponseEntity
//            .status(code.status())
//            .body(CommonResponse.error(code));
//    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonResponse<ResponseCode>> handleIllegalArgumentException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(CommonResponse.error(ResponseCode.UNAUTHORIZED));
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<CommonResponse<ResponseCode>> handleAlreadyExistException(AlreadyExistException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(CommonResponse.error(ResponseCode.ALREADY_EXIST));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CommonResponse<ResponseCode>> handleNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(CommonResponse.error(ResponseCode.NOT_FOUND));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CommonResponse<ResponseCode>> handleNullPointerException(NullPointerException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(CommonResponse.error(ResponseCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<ResponseCode>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(CommonResponse.error(ResponseCode.BAD_REQUEST));
    }

    @ExceptionHandler(MailSendFailureException.class)
    public ResponseEntity<CommonResponse<ResponseCode>> handleMailSendFailureException(MailSendFailureException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity
            .status(500)
            .body(CommonResponse.error(ResponseCode.MAIL_SEND_FAIL));
    }

    

}
