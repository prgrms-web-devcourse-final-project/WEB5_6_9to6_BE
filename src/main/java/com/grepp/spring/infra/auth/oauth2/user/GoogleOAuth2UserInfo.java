package com.grepp.spring.infra.auth.oauth2.user;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleOAuth2UserInfo implements OAuth2UserInfo{

    private final Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }


}
