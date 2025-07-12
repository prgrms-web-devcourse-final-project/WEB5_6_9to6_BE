package com.grepp.spring.infra.util;

import com.grepp.spring.app.model.auth.domain.Principal;
import com.grepp.spring.infra.error.exceptions.UnauthenticatedAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    private SecurityUtil() {}

    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Principal)) {
            throw new UnauthenticatedAccessException("Security context에 인증 정보가 없습니다.");
        }
        Principal principal = (Principal) authentication.getPrincipal();
        return principal.getMemberId();
    }
}