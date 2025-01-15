package com.jyx.eshopbackend.security.authenticationtoken;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String username;
    private final String requestName;
    private final Collection<GrantedAuthority> authorities;

    public JwtAuthenticationToken(Object username, Object token, Collection<GrantedAuthority> authorities) {
        super(username, token, authorities);
        this.username = String.valueOf(username);
        this.requestName = String.valueOf(token);
        this.authorities = authorities;
    }


    public String getUsername() {
        return username;
    }

    public String getRequestName() {
        return requestName;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}