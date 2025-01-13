package com.jyx.eshopbackend;

import com.jyx.eshopbackend.security.PublicUrl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

}
