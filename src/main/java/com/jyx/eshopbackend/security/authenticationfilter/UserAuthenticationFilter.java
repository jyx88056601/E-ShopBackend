package com.jyx.eshopbackend.security.authenticationfilter;

import com.jyx.eshopbackend.security.UserPrincipal;
import com.jyx.eshopbackend.security.authenticationprovider.UserAuthenticationProvider;
import com.jyx.eshopbackend.security.authenticationtoken.UserAuthenticationToken;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

   private final UserDetailsService userDetailsService;

   private final UserAuthenticationProvider userAuthenticationProvider;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserAuthenticationFilter(UserDetailsService userDetailsService, UserAuthenticationProvider userAuthenticationProvider) {
        this.userDetailsService = userDetailsService;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }



    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {


        UserPrincipal userPrincipal;
        try {
            String username = request.getHeader("username");
            if (username == null) {
                logger.warn("Username is missing for request: {}", request.getRequestURI());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username is missing");
                return;
            }
            userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(username);
            logger.info("Username {} found for request: {}", username, request.getRequestURI());
        } catch (UsernameNotFoundException e) {
            logger.error("Username not found: {} for request: {}", request.getHeader("username"), request.getRequestURI(), e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Username not found: " + e.getMessage());
            return;
        }

        UserAuthenticationToken authentication;
        try {
            String password = request.getHeader("password");
            if (password == null) {
                logger.warn("Password is missing for request: {}", request.getRequestURI());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password is missing");
                return;
            }
            authentication = (UserAuthenticationToken) userAuthenticationProvider.authenticate(new UserAuthenticationToken(userPrincipal, password));
            logger.info("Authentication successful for username: {}", userPrincipal.getUsername());
        } catch (BadCredentialsException e) {
            logger.error("Bad credentials for username: {} for request: {}", request.getHeader("username"), request.getRequestURI(), e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Bad credentials: " + e.getMessage());
            return;
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Authentication failed for username: {} for request: {}", request.getHeader("username"), request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("Authentication successful for username: {}. Setting security context.", userPrincipal.getUsername());

        filterChain.doFilter(request, response);
    }



}
