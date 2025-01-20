package com.jyx.eshopbackend.security.authenticationprovider;

import com.jyx.eshopbackend.security.authenticationtoken.JwtAuthenticationToken;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


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
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("JwtAuthenticationProvider.authenticate...");
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        if (!jwtAuthenticationToken.getUsername().equals(jwtAuthenticationToken.getRequestName()) ) {
                logger.info("Token is invalid");
                throw new BadCredentialsException("username does not match");
        }
        logger.info("JWT authenticated, token is valid");
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }


}

