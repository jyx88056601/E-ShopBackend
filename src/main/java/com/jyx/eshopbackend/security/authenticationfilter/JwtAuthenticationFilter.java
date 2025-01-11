package com.jyx.eshopbackend.security.authenticationfilter;

import com.jyx.eshopbackend.security.authenticationprovider.JwtAuthenticationProvider;
import com.jyx.eshopbackend.security.authenticationtoken.JwtAuthenticationToken;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public JwtAuthenticationFilter(JwtAuthenticationProvider jwtAuthenticationProvider) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {

        Optional<String> token = extractTokenFromRequest(request);

        // 如果没有 token，直接进入下一个 filter 进行账户密码验证
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        JwtAuthenticationToken authentication;
        try {
            // 使用 token 进行认证
            authentication = (JwtAuthenticationToken) jwtAuthenticationProvider.authenticate(new JwtAuthenticationToken(request.getHeader("username"), token.get()));
        } catch (AuthenticationException e) {
            // 认证失败时返回 401 错误并记录日志
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + e.getMessage());
            LoggerFactory.getLogger(this.getClass()).warn("Authentication failed for request: {}", request.getRequestURI());
            return;
        }

        // 如果认证成功，设置 SecurityContext，并继续过滤链
        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            filterChain.doFilter(request, response);
            return;
        }

        // 记录请求处理时间
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, response);
        long duration = System.currentTimeMillis() - startTime;

        // 打印日志：请求路径，处理时间，以及客户端 IP
        String clientIp = request.getRemoteAddr();
        LoggerFactory.getLogger(this.getClass()).info("Request URI: {} | Duration: {} ms | Client IP: {}", request.getRequestURI(), duration, clientIp);
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
