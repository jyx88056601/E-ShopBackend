package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.Product;

public class ProductSimplifiedResponseDTO {

    private final String product_id;
    private final String name;

    private final String price;

    private final String mainPictureUrl;

    public ProductSimplifiedResponseDTO(Product product) {
        this.product_id = String.valueOf(product.getId());
        this.name = product.getName();
        this.price = String.valueOf(product.getPrice());
        this.mainPictureUrl = product.getMainPictureUrl().replace("https://e-commerce-shop-ethan-jiang.s3.us-west-2.amazonaws.com/",
                "https://e-commerce-shop-ethan-jiang.s3.us-west-2.amazonaws.com/product/");
    }

    public String getMainPictureUrl() {
        return mainPictureUrl;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
