package com.grepp.spring.infra.config.security;

import com.grepp.spring.infra.auth.jwt.JwtAuthenticationEntryPoint;
import com.grepp.spring.infra.auth.jwt.filter.JwtAuthenticationFilter;
import com.grepp.spring.infra.auth.jwt.filter.JwtExceptionFilter;
import com.grepp.spring.infra.auth.oauth2.OAuth2FailureHandler;
import com.grepp.spring.infra.auth.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .oauth2Login(
                oauth -> oauth
                    .successHandler(oAuth2SuccessHandler)
                    .failureHandler(oAuth2FailureHandler)
            )
            .cors(Customizer.withDefaults())
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .logout(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(
                (requests) -> requests
                    .requestMatchers("/favicon.ico", "/img/**", "/js/**","/css/**").permitAll()
                    .requestMatchers("/", "/error", "/auth/login", "/auth/signup").permitAll()
                    .requestMatchers("/api/v1/studies/search", "/api/v1/studies/categories").permitAll()
                    .requestMatchers("/api/v1/studies/*").permitAll()
                    .requestMatchers("/api/v1/studies/*/notification").permitAll()
                    .requestMatchers("/api/v1/studies/*/members").permitAll()
                    .requestMatchers("/api/v1/studies/*/goals").permitAll()
                    .anyRequest().authenticated()
//                    .anyRequest().permitAll()
            )
            // jwtAuthenticationEntryPoint 는 oauth 인증을 사용할 경우 제거
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                            .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        .requestMatchers("/ws-connect/**");
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
