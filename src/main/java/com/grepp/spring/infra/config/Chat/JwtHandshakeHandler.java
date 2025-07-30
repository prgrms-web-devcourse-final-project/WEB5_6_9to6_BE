package com.grepp.spring.infra.config.Chat;


import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

// 채팅서버 분리시 사용

public class JwtHandshakeHandler extends DefaultHandshakeHandler {


//    @Override
//    protected Principal determineUser(ServerHttpRequest request,
//        WebSocketHandler wsHandler,
//        Map<String, Object> attributes) {
//        List<String> authHeaders = request.getHeaders().get("Authorization");
//
//        if (authHeaders != null && !authHeaders.isEmpty()) {
//            String token = authHeaders.get(0).substring("Bearer ".length());
//
//            // JWT 파싱 및 검증
//            Principal principal = jwtProvider.getPrincipal(token); // 직접 구현한 jwtProvider 등
//
//            return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
//        }
//        return null;
//    }
}
