package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.Product;

import java.time.LocalDateTime;

public class ProductUpLoadingResponseDTO extends ProductDTO{
    private final LocalDateTime createdTime;

    public ProductUpLoadingResponseDTO(Product product) {
        super(product.getName(), String.valueOf(product.getPrice()), String.valueOf(product.getStock()), product.getCategory());
        this.createdTime =  product.getCreatedTime();
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
}
