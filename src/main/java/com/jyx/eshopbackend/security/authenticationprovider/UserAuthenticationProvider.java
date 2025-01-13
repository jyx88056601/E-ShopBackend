package com.jyx.eshopbackend.security.authenticationprovider;

import com.jyx.eshopbackend.security.UserPrincipal;
import com.jyx.eshopbackend.security.authenticationtoken.UserAuthenticationToken;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);
    private final PasswordEncoder passwordEncoder;

    public UserAuthenticationProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("UserAuthenticationProvider.authenticate...");
        // if user already has a jwt authentication and that is valid then we return the authentication immediately
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            logger.info("UserAuthenticationProvider: Already authenticated with JWT token");
            return authentication;
        }
        // otherwise
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if (!passwordEncoder.matches(String.valueOf(authentication.getCredentials()),userPrincipal.getPassword())) {
            logger.info("UserAuthenticationProvider: user and password do not match");
            throw new BadCredentialsException("Incorrect password");
        }
        logger.info("UserAuthenticationProvider: authenticated");
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UserAuthenticationToken.class);
    }
}
