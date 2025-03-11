package com.jyx.eshopbackend;

import com.jyx.eshopbackend.service.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EShopBackendApplication {


    public static void main(String[] args) {
       var context = SpringApplication.run(EShopBackendApplication.class, args);
       var productService = context.getBean(ProductService.class);

    }

}
