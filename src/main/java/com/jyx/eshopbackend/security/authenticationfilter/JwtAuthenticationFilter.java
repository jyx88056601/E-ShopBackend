package com.jyx.eshopbackend.security.authenticationfilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.jyx.eshopbackend.security.authenticationprovider.JwtAuthenticationProvider;
import com.jyx.eshopbackend.security.authenticationtoken.JwtAuthenticationToken;
import com.jyx.eshopbackend.security.jwtservice.JwtUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtil jwt;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public JwtAuthenticationFilter(JwtUtil jwt, JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwt = jwt;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {


        Optional<String> token = extractTokenFromRequest(request);
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        JwtAuthenticationToken jwtAuthenticationToken;
        try {
            logger.info("Decoding token");
            DecodedJWT DecodedJWT = jwt.decodeToken(token.get());
            String username = DecodedJWT.getSubject();
            logger.info("Token decode with username : " + username);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(DecodedJWT.getClaim("role").toString()));
            jwtAuthenticationToken = new JwtAuthenticationToken(username,request.getHeader("username"), authorities);
        } catch (Exception e) {
            logger.info("Token can't be decoded");
            filterChain.doFilter(request, response);
            throw new BadCredentialsException("Decoding token failed", e);
        }

        Authentication authentication;
        try {
             authentication =  jwtAuthenticationProvider.authenticate(jwtAuthenticationToken);
        } catch (AuthenticationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + e.getMessage());
            LoggerFactory.getLogger(this.getClass()).warn("Authentication failed for request: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);

    }


    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {

        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            logger.debug("Extracted JWT: {}" + token);
            return Optional.of(token.substring(7));
        }
        logger.debug("Authorization header missing or invalid");
        return Optional.empty();
    }
}
