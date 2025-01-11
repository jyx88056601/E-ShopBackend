package com.jyx.eshopbackend.security;

import com.jyx.eshopbackend.security.authenticationfilter.JwtAuthenticationFilter;
import com.jyx.eshopbackend.security.authenticationfilter.UserAuthenticationFilter;
import com.jyx.eshopbackend.security.authenticationprovider.JwtAuthenticationProvider;
import com.jyx.eshopbackend.security.authenticationprovider.UserAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UserAuthenticationFilter userAuthenticationFilter;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final UserAuthenticationProvider userAuthenticationProvider;

    public AppSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, UserAuthenticationFilter userAuthenticationFilter, JwtAuthenticationProvider jwtAuthenticationProvider, UserAuthenticationProvider userAuthenticationProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userAuthenticationFilter = userAuthenticationFilter;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(
                        authorizeHttp -> {
                            authorizeHttp.requestMatchers("/admin/**").hasRole("ADMIN");
                            authorizeHttp.requestMatchers("/").permitAll();
                            authorizeHttp.requestMatchers("/error").permitAll();
                            authorizeHttp.anyRequest().authenticated();
                        }
                )
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, AuthenticationFilter.class)
                .addFilterAfter(userAuthenticationFilter, AuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(userAuthenticationProvider);
        builder.authenticationProvider(jwtAuthenticationProvider);
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
