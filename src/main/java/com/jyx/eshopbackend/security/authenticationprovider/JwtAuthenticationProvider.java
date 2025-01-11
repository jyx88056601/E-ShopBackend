package com.jyx.eshopbackend.security.authenticationprovider;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jyx.eshopbackend.security.authenticationtoken.JwtAuthenticationToken;
import com.jyx.eshopbackend.security.jwtservice.JwtUtil;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;


// authenticationManager manages a list of providers(jwtAuthentication Provider, userAuthentication Provider etc.)
// a filter chain usually has one authentication manager when authentication manager is created by HttpSecurity http
// @Bean
// public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        builder.authenticationProvider(userAuthenticationProvider);
//        builder.authenticationProvider(jwtAuthenticationProvider);
//        return builder.build();
//        }
// provider verify the credential with input and set the authentication to true if it is verified
// or return null, which means it can't pass the current verification, but we want to move on
// or throw Exception, and terminate filter chain because the token is incorrect.

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    private final JwtUtil jwt;

    public JwtAuthenticationProvider(JwtUtil jwt) {
        this.jwt = jwt;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String requestName = String.valueOf(jwtAuthenticationToken.getUsername());
        try {
            DecodedJWT DecodedJWT = jwt.decodeToken(String.valueOf(jwtAuthenticationToken.getToken()));
            String username = DecodedJWT.getSubject();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(DecodedJWT.getClaim("role").toString()));

            if (!username.equals(requestName)) {
                throw new BadCredentialsException("username does not match");
            }
            ((JwtAuthenticationToken) authentication).setAuthorities(authorities);
            authentication.setAuthenticated(true);
            return authentication;
        } catch (Exception e) {
            throw new BadCredentialsException("Decoding token failed", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }


}

