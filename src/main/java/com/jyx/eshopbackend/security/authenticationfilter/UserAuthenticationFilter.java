package com.jyx.eshopbackend.security.authenticationfilter;

import com.jyx.eshopbackend.security.PublicUrl;
import com.jyx.eshopbackend.security.UserPrincipal;
import com.jyx.eshopbackend.security.authenticationprovider.UserAuthenticationProvider;
import com.jyx.eshopbackend.security.authenticationtoken.UserAuthenticationToken;
import com.jyx.eshopbackend.service.UserDetailService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserAuthenticationFilter.class);

   private final UserDetailService userDetailService;

   private final UserAuthenticationProvider userAuthenticationProvider;

   private final PublicUrl publicUrl;

    public UserAuthenticationFilter(PublicUrl publicUrl, UserDetailService userDetailService, UserAuthenticationProvider userAuthenticationProvider) {
        this.publicUrl = publicUrl;
        this.userDetailService = userDetailService;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }



    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        logger.info(request.getRequestURI());
        for(String url : publicUrl.urls()) {
            if (request.getRequestURI().equals(url)) {
                logger.info(url + " is permitted by default, move to the next filter");
                filterChain.doFilter(request, response);
                return;
            }
        }


        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            logger.info("JWT authentication has been set to true");
            filterChain.doFilter(request, response);
            return;
        }
        logger.info("Starting user-password matching...");
        UserPrincipal userPrincipal;
        try {
            String username = request.getParameter("username");
            if (username.isEmpty()) {
                logger.info("UserAuthenticationFilter: Username is missing");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username is missing");
                return;
            }
            userPrincipal = (UserPrincipal) userDetailService.loadUserByUsername(username);
            logger.info("find user from database : " + userPrincipal.getUsername());
            boolean isAdmin = userPrincipal.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
               if(isAdmin) {
                   logger.info("Admin account");
               }
            if (!isAdmin && !userPrincipal.isAccountNonLocked()) {
                logger.info("Account is locked");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account is locked");
                return;
            }
        } catch (UsernameNotFoundException e) {
           logger.error("User not found");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Username not found: " + e.getMessage());
            return;
        }

        UserAuthenticationToken authentication;
        try {
            String password = request.getParameter("password");
            if (password.isEmpty()) {
                logger.info("Password is missing");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password is missing");
                return;
            }
            UserAuthenticationToken userAuthenticationToken = new UserAuthenticationToken(userPrincipal, password, userPrincipal.getAuthorities());
            authentication = (UserAuthenticationToken) userAuthenticationProvider.authenticate(userAuthenticationToken);
        } catch (BadCredentialsException e) {
            logger.error("Bad credentials for username: {} for request: {}", request.getParameter("username"), request.getRequestURI(), e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Bad credentials: " + e.getMessage());
            return;
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.error("UserAuthenticationFilter: Authentication failed");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed");
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("Authentication successful for username: {}. Setting security context.", userPrincipal.getUsername());

        filterChain.doFilter(request, response);
    }



}
