package com.grepp.spring.app.model.auth.domain;

import com.grepp.spring.app.model.member.entity.Member;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class Principal extends User {

    private Long memberId;
    private String accessToken;

    public Principal(Long memberId, String username, String password,
        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.memberId = memberId;
    }

    public static Principal createPrincipal(Member member,
        List<SimpleGrantedAuthority> authorities){
        return new Principal(member.getId(), member.getEmail(), member.getPassword(), authorities);
    }
    
    public Optional<String> getAccessToken() {
        return Optional.of(accessToken);
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
