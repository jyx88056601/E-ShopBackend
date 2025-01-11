package com.jyx.eshopbackend.security.jwtservice;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jyx.eshopbackend.model.Role;
import com.jyx.eshopbackend.security.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtUtil {
    private final JwtConfig jwtConfig;

    public JwtUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(UserPrincipal userPrincipal) {
        Role role = Role.ROLE_USER;
        for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
            role = Role.valueOf(authority.getAuthority());
        }
        return JWT.create()
                .withSubject(userPrincipal.getUsername())
                .withClaim("role", role.name())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(Duration.of(30, ChronoUnit.MINUTES)))
                .sign(Algorithm.HMAC256(jwtConfig.getSecretKey()));
    }

    public DecodedJWT decodeToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(jwtConfig.getSecretKey()))
                    .build()
                    .verify(token);
        } catch (Exception e) {
            System.out.println("can't decode token !");
            throw new RuntimeException("Invalid or expired token", e);
        }
    }

}
