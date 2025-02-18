package com.jyx.eshopbackend.security;

import com.jyx.eshopbackend.security.authenticationfilter.JwtAuthenticationFilter;
import com.jyx.eshopbackend.security.authenticationfilter.UserAuthenticationFilter;
import com.jyx.eshopbackend.security.authenticationprovider.JwtAuthenticationProvider;
import com.jyx.eshopbackend.security.authenticationprovider.UserAuthenticationProvider;
import org.springframework.web.filter.CorsFilter;
import org.apache.catalina.security.SecurityConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
// disable basic auth
public class AppSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UserAuthenticationFilter userAuthenticationFilter;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final UserAuthenticationProvider userAuthenticationProvider;

    private final PublicUrl publicUrl;

    public AppSecurityConfig(PublicUrl publicUrl, JwtAuthenticationFilter jwtAuthenticationFilter, UserAuthenticationFilter userAuthenticationFilter, JwtAuthenticationProvider jwtAuthenticationProvider, UserAuthenticationProvider userAuthenticationProvider) {
        this.publicUrl = publicUrl;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userAuthenticationFilter = userAuthenticationFilter;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

// 限制分别来自 authorizeHttp
        // 以及userAuthenticationFilter 的限制  两个都需要突破
            //jwtAuthenticationFilter 却没事 因为check了token
        return http
                .authorizeHttpRequests(
                        authorizeHttp -> {
                            logger.info("Public URLs: {}", String.join(", ", publicUrl.urls()));
                            authorizeHttp.requestMatchers(publicUrl.urls()).permitAll();
                            logger.info("Configuring /admin/** path to require ROLE_ADMIN authority");
                            authorizeHttp.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN");
                            logger.info("Configuring /business/** path to require ROLE_SELLER authority");
                            authorizeHttp.requestMatchers("/business/**").hasAuthority("ROLE_SELLER");
                            logger.info("Configuring /personal/** path to require ROLE_BUYER authority");
                            authorizeHttp.requestMatchers("/personal/**").hasAuthority("ROLE_BUYER");
                            logger.info("All other requests will require authentication");
                            authorizeHttp.anyRequest().authenticated();
                        }
                )
                .headers(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, AuthenticationFilter.class)
                .addFilterAfter(userAuthenticationFilter, JwtAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")         // 删除指定的 Cookie（如 Session ID）
                )
                .build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(userAuthenticationProvider)
                .authenticationProvider(jwtAuthenticationProvider);
        return builder.build();
    }




    /*
     * Global AuthenticationManager bean that combines multiple AuthenticationProviders.
     * This is independent of the current HttpSecurity configuration and can be reused
     * across different SecurityFilterChains or other components.

    @Bean
    public AuthenticationManager authenticationManager() {
        // Create an AuthenticationManager with the specified AuthenticationProviders
        return new ProviderManager(
                List.of(jwtAuthenticationProvider, userAuthenticationProvider)
        );
    }
     */


}
