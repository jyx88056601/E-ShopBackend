package com.jyx.eshopbackend.security.authenticationtoken;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String username;
    private final String token;
    private Collection<GrantedAuthority> authorities;

    public JwtAuthenticationToken(Object username, Object token ) {
        super(username, token);
        this.username = String.valueOf(username);
        this.token = String.valueOf(token);
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}