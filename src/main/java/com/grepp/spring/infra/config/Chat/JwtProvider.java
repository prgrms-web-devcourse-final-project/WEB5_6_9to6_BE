package com.grepp.spring.infra.config.Chat;

import com.grepp.spring.app.model.auth.domain.Principal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import io.jsonwebtoken.Jwts;

// 채팅 서버 분리 시 사용.


public class JwtProvider {

//    private final Key key;
//
//    public JwtProvider(@Value("${jwt.secret}") String secret) {
//        this.key = Keys.hmacShaKeyFor(secret.getBytes());
//    }
//
//    public UsernamePasswordAuthenticationToken getPrincipal(String token) {
//        Claims claims = parseClaims(token);
//
//        Long memberId = claims.get("memberId", Long.class);
//        String email = claims.getSubject();
//        String role = claims.get("role", String.class);
//
//        Principal principal = new Principal(memberId, email, role);
//
//        return new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
//    }
//
//    private Claims parseClaims(String token) {
//        return Jwts.parserBuilder()
//            .setSigningKey(key)
//            .build()
//            .parseClaimsJws(token)
//            .getBody();
//    }

}
