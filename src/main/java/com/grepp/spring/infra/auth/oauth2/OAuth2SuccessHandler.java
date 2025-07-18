package com.grepp.spring.infra.auth.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.spring.app.model.auth.AuthService;
import com.grepp.spring.app.model.auth.code.AuthToken;
import com.grepp.spring.app.model.auth.code.Role;
import com.grepp.spring.app.model.auth.dto.TokenDto;
import com.grepp.spring.app.model.member.repository.MemberRepository;
import com.grepp.spring.app.model.member.code.SocialType;
import com.grepp.spring.app.model.member.entity.Member;
import com.grepp.spring.infra.auth.jwt.JwtTokenProvider;
import com.grepp.spring.infra.auth.jwt.TokenCookieFactory;
import com.grepp.spring.infra.auth.oauth2.user.OAuth2UserInfo;
import com.grepp.spring.infra.response.CommonResponse;
import com.grepp.spring.infra.response.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @Value("${app.frontend.signup-redirect}")
    private String signupRedirectUrl;

    @Value("${app.frontend.signup-redirect-main}")
    private String signupRedirectMainUrl;


    /* TODO 현재 Oauth2 로그인 시 roles 에는
        "OAUTH2_USER,SCOPE_https://www.googleapis.com/auth/userinfo.email,SCOPE_https://www.googleapis.com/auth/userinfo.profile,SCOPE_openid"
        이런 식으로 달림
        security 에서 hasAnyRole 로 ROLE_USER 와 OAUTH2_USER 를 항상 같이 넣을 지 아님 jwt 만들 때 ROLE_USER 를 넣을 수 있을 지 고민하기
     */

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        String registrationId = null;

        // OAuth2AuthenticationToken에서 registrationId를 추출합니다.
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            registrationId = oauthToken.getAuthorizedClientRegistrationId();
        }

        if (registrationId == null) {
            // registrationId를 가져오지 못하면 예외를 발생시키거나 적절히 처리합니다.
            throw new ServletException("OAuth2 registration ID를 확인할 수 없습니다.");
        }

        // 수정된 OAuth2UserInfo.create 메서드에 registrationId를 전달합니다.
        OAuth2UserInfo userInfo = OAuth2UserInfo.create(registrationId, user);

        String roles = String.join(",", authentication.getAuthorities()
            .stream().map(GrantedAuthority::getAuthority).toList());
        if (!roles.contains("ROLE_USER")) {
            roles += ",ROLE_USER";
        }

        // 처음 로그인한다면 Member 저장
        Optional<Member> existMember = memberRepository.findByEmail(userInfo.getEmail());
        if (existMember.isEmpty() || (existMember.isPresent() && existMember.get().getBirthday() == null)) {

            if (existMember.isEmpty()) {
                SocialType provider =  switch (userInfo.getProvider()) {
                    case "google" -> SocialType.GOOGLE;
                    case "kakao" -> SocialType.KAKAO;
                    default -> SocialType.LOCAL;
                };

                Member member = Member.builder()
                    .email(userInfo.getEmail())
                    .password("{noop} dummy-password")
                    .role(Role.ROLE_USER)
                    .rewardPoints(100)
                    .winCount(0)
                    .socialType(provider)
                    .build();
                memberRepository.save(member);
            }

            // 토큰 발급 및 리다이렉트
            TokenDto token = authService.processTokenSignin(userInfo.getEmail(), roles);

            ResponseCookie accTkCookie = TokenCookieFactory.create(
                AuthToken.ACCESS_TOKEN.name(), token.getAccessToken(),
                jwtTokenProvider.getAccessTokenExpiration()
            );
            ResponseCookie rfTkCookie = TokenCookieFactory.create(
                AuthToken.REFRESH_TOKEN.name(), token.getRefreshToken(),
                token.getRefreshExpiresIn()
            );

            response.addHeader("Set-Cookie", accTkCookie.toString());
            response.addHeader("Set-Cookie", rfTkCookie.toString());

            getRedirectStrategy().sendRedirect(request, response, signupRedirectUrl);
            return;
        }

        // 현재 같은 이메일의 로컬 계정이 존재할때 로그인 거부 및 로컬 로그인 안내
        Member member = existMember.orElseThrow(); // NPE 방지용
        if (member.getSocialType() == SocialType.LOCAL) {

            CommonResponse<?> errorResponse = CommonResponse.error(ResponseCode.SOCIAL_LOGIN_CONFLICT);
            String json = new ObjectMapper().writeValueAsString(errorResponse);

            response.getWriter().write(json);
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409
            response.getWriter().flush();
            return;
        }

        TokenDto token = authService.processTokenSignin(userInfo.getEmail(), roles);
        ResponseCookie accTkCookie = TokenCookieFactory.create(AuthToken.ACCESS_TOKEN.name(),
            token.getAccessToken(),
            jwtTokenProvider.getAccessTokenExpiration()
        );
        ResponseCookie rfTkCookie = TokenCookieFactory.create(
            AuthToken.REFRESH_TOKEN.name(),
            token.getRefreshToken(),
            token.getRefreshExpiresIn()
        );

        // NOTE aws api gateway 에서 Set-Cookie 병합 정핵이 다를 수 있으니 주의 aws 에서만 jwt 관련 오류가 생긴하면 해당 부분도 확인하기
        response.addHeader("Set-Cookie", accTkCookie.toString());
        response.addHeader("Set-Cookie", rfTkCookie.toString());
        getRedirectStrategy().sendRedirect(request, response, signupRedirectMainUrl);
    }
}