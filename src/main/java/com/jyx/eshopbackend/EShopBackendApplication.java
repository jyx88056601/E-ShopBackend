package com.jyx.eshopbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EShopBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EShopBackendApplication.class, args);
    }

}
