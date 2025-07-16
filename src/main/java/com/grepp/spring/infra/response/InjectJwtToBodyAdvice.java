//package com.grepp.spring.infra.response;
//
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//@RestControllerAdvice
//public class InjectJwtToBodyAdvice implements ResponseBodyAdvice<CommonResponse<?>>  {
//
//    private static final String BEARER_PREFIX = "Bearer ";
//
//    @Override
//    public boolean supports(MethodParameter returnType,
//        Class<? extends HttpMessageConverter<?>> converterType) {
//        return returnType.getParameterType().isAssignableFrom(CommonResponse.class);
//    }
//
//    @Override
//    public CommonResponse<?> beforeBodyWrite(CommonResponse<?> body, MethodParameter returnType,
//        MediaType selectedContentType,
//        Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
//        ServerHttpResponse response) {
//        // Authorization 획득
//        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        // JWT 있는 경우
//        if (authHeader != null && !authHeader.isBlank()) {
//            String token = authHeader;
//            // Bearer 가 있다면 제거
//            if (authHeader.startsWith(BEARER_PREFIX)) {
//                token = authHeader.substring(BEARER_PREFIX.length());
//            }
//            return body.withToken(token);
//        }
//
//        // 요청 헤더 JWT 가 없는 경우 그대로 반환
//        return body;
//    }
//
//}
