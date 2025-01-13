package com.jyx.eshopbackend.service;

import com.jyx.eshopbackend.security.authenticationtoken.UserAuthenticationToken;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserSignInService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserSignInService.class);

    private  final AuthenticationManager authenticationManager;

    public UserSignInService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Authentication login(UserAuthenticationToken authentication) {
        logger.info("UserSignInService: authenticationManager.authenticate...");
        Authentication authenticated = authenticationManager.authenticate(authentication);
        logger.info("Authenticated? " + authenticated.isAuthenticated());
        return authenticated;
    }
}
