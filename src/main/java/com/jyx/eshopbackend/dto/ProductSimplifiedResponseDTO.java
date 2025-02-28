package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.Product;

public class ProductSimplifiedResponseDTO {

    private final String product_id;
    private final String name;

    private final String price;

    private final String mainPictureUrl;

    private final String stock;
    private final String merchantId;

    public ProductSimplifiedResponseDTO(Product product) {
        this.product_id = String.valueOf(product.getId());
        this.name = product.getName();
        this.price = String.valueOf(product.getPrice());
        this.mainPictureUrl = product.getMainPictureUrl().replace("https://e-commerce-shop-ethan-jiang.s3.us-west-2.amazonaws.com/",
                "https://e-commerce-shop-ethan-jiang.s3.us-west-2.amazonaws.com/product/");
        this.stock = String.valueOf(product.getStock());
       this.merchantId = String.valueOf(product.getOwnerId());
    }

    public String getMerchantId() {
        return merchantId;
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

    public String getStock() {
        return stock;
    }
}
