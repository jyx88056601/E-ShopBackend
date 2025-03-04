package com.jyx.eshopbackend;

import com.jyx.eshopbackend.security.PublicUrl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EShopBackendApplicationConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public PublicUrl publicUrl() {
        return new PublicUrl(new String[]{"/", "/error", "/signup", "/logout"});
    }


    @Bean
    public WebClient paypalWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl("https://api-m.sandbox.paypal.com")
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
    }

//    @Bean
//    public InitializingBean initializingBean() {
//        return () -> {
//            SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
//        };
//    }

}
