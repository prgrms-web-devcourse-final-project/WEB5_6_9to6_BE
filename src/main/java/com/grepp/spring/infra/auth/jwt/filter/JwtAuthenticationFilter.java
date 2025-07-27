package com.grepp.spring.infra.auth.jwt.filter;

import com.grepp.spring.app.model.auth.code.AuthToken;
import com.grepp.spring.app.model.auth.token.RefreshTokenService;
import com.grepp.spring.app.model.auth.token.UserBlackListRepository;
import com.grepp.spring.app.model.auth.token.entity.RefreshToken;
import com.grepp.spring.app.model.auth.token.entity.UserBlackList;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.infra.auth.jwt.JwtTokenProvider;
import com.grepp.spring.infra.auth.jwt.TokenCookieFactory;
import com.grepp.spring.infra.auth.jwt.dto.AccessTokenDto;
import com.grepp.spring.infra.error.exceptions.CommonException;
import com.grepp.spring.infra.response.ResponseCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final RefreshTokenService refreshTokenService;
    private final UserBlackListRepository userBlackListRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // 정적 경로 리스트
        List<String> staticExcludePaths = List.of(
            "/auth/signup", "/auth/login", "/favicon.ico", "/img", "/js", "/css", "/download",
            "/error", "/api/member/exists", "/member/signin", "/member/signup",
            "/api/v1/studies/search", "/api/v1/studies/categories"
        );

        // 정적 경로
        if (staticExcludePaths.stream().anyMatch(path::startsWith)) {
            return true;
        }

        // 동적 경로 리스트
        List<String> dynamicExcludePatterns = List.of(
            "^/api/v1/studies/\\d+$",                // /api/v1/studies/{studyId}
            "^/api/v1/studies/\\d+/notification$",   // /api/v1/studies/{studyId}/notification
            "^/api/v1/studies/\\d+/members$",        // /api/v1/studies/{studyId}/members
            "^/api/v1/studies/\\d+/goals$"           // /api/v1/studies/{studyId}/goals
        );

        // matches로 정규식 패턴을 확인
        return dynamicExcludePatterns.stream().anyMatch(path::matches);
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.resolveToken(request, AuthToken.ACCESS_TOKEN);
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            if (jwtTokenProvider.validateToken(accessToken, request)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                if (userBlackListRepository.existsById(authentication.getName())) {
                    filterChain.doFilter(request, response);
                    return;
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            manageTokenRefresh(accessToken, request, response);
        }
        filterChain.doFilter(request, response);
    }
    
    private void manageTokenRefresh(
        String accessToken,
        HttpServletRequest request,
        HttpServletResponse response) throws IOException {
        
        Claims claims  = jwtTokenProvider.getClaims(accessToken);
        if (userBlackListRepository.existsById(claims.getSubject())) {
            return;
        }
        
        String refreshToken = jwtTokenProvider.resolveToken(request, AuthToken.REFRESH_TOKEN);
        RefreshToken rt = refreshTokenService.findByAccessTokenId(claims.getId());
        
        if(rt == null) return;
        
        if (!rt.getToken().equals(refreshToken)) {
            userBlackListRepository.save(new UserBlackList(claims.getSubject()));
            throw new CommonException(ResponseCode.SECURITY_INCIDENT);
        }
        
        addToken(response, claims, rt);
    }
    
    private void addToken(HttpServletResponse response, Claims claims, RefreshToken refreshToken) {
        String username = claims.getSubject();
        long id = memberRepository.findIdByEmail(username);
        log.info("id: {}", id);
        AccessTokenDto newAccessToken = jwtTokenProvider.generateAccessToken(username,
            (String) claims.get("roles"), id);
        Authentication authentication = jwtTokenProvider.getAuthentication(newAccessToken.getToken());
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        RefreshToken newRefreshToken = refreshTokenService.renewingToken(refreshToken.getAtId(), newAccessToken.getJti());
        
        ResponseCookie accessTokenCookie = TokenCookieFactory.create(AuthToken.ACCESS_TOKEN.name(),
            newAccessToken.getToken(), jwtTokenProvider.getAccessTokenExpiration());
        
        ResponseCookie refreshTokenCookie = TokenCookieFactory.create(
            AuthToken.REFRESH_TOKEN.name(),
            newRefreshToken.getToken(),
            newRefreshToken.getTtl());
        
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }
}
