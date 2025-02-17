package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.Product;
import com.jyx.eshopbackend.model.ProductImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductDetailDTO extends ProductDTO{

    private final String id;

    private final List<String> awsUrls;

    private final String createdTime;
    private final String updatedTime;

    public ProductDetailDTO(Product product) {
        super(product.getName(), String.valueOf(product.getPrice()), String.valueOf(product.getStock()), product.getCategory());
        this.createdTime = product.getCreatedTime().toString();
        this.updatedTime = product.getUpdatedTime().toString();
        this.id = String.valueOf(product.getId());
        List<String> imageUrls = new ArrayList<>();
        for (ProductImage productImage : product.getProductImages()) {
            imageUrls.add(productImage.getUrl());
        }
        this.awsUrls = Collections.unmodifiableList(imageUrls);
    }

    public String getId() {
        return id;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public List<String> getAwsUrls() {
        return awsUrls;
    }

}
