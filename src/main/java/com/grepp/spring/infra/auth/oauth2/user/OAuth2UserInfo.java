package com.grepp.spring.infra.auth.oauth2.user;

import com.grepp.spring.infra.error.exceptions.NotServiceAuthServerException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfo {

    String getProvider();
    String getProviderId();
    String getName();
    String getEmail();

    static  OAuth2UserInfo create(String registrationId, OAuth2User user) {
        switch (registrationId) {
            case "kakao" -> {
                return new KakaoOAuth2UserInfo(user.getAttributes());
            }
            case "google" -> {
                return new GoogleOAuth2UserInfo(user.getAttributes());
            }
        }
        throw new NotServiceAuthServerException("지원하지 않는 인증 서버입니다.");
    }
}