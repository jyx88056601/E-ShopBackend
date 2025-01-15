package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.security.authenticationtoken.UserAuthenticationToken;
import com.jyx.eshopbackend.security.jwtservice.JwtUtil;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserSignInService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserSignInService.class);
    private final JwtUtil jwt;

    public UserSignInService(JwtUtil jwt) {
        this.jwt = jwt;
    }

    public String generateToken() {
        UserAuthenticationToken authentication = (UserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authenticated and return jwt token");
        return jwt.generateToken(authentication.getPrincipal());
    }
}
