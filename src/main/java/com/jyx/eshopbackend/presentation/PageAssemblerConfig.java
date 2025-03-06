package com.jyx.eshopbackend.presentation;

import com.jyx.eshopbackend.dto.OrderResponseDTO;
import com.jyx.eshopbackend.dto.ProductDetailDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PagedResourcesAssembler;

@Configuration
public class PageAssemblerConfig {
    @Bean
    public PagedResourcesAssembler<OrderResponseDTO> orderPageAssembler() {
        return new PagedResourcesAssembler<>(null, null);
    }

    @Bean
    public PagedResourcesAssembler<ProductDetailDTO> productDetailPageAssembler() {
        return new PagedResourcesAssembler<>(null, null);
    }
}
