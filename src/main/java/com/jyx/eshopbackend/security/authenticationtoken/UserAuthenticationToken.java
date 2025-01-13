package com.jyx.eshopbackend.security.authenticationtoken;

import com.jyx.eshopbackend.security.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final Collection<GrantedAuthority> authorities;

    public UserAuthenticationToken(UserPrincipal principal, Object credentials, Collection<GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.authorities = authorities;
    }


    public UserPrincipal getPrincipal() {
        return (UserPrincipal) super.getPrincipal();
    }


    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

}