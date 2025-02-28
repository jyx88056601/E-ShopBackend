package com.jyx.eshopbackend.dto;

public class OrderItemDTO {
   private final String productId;
   private final String quantity;

    public OrderItemDTO(String productId, String quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getQuantity() {
        return quantity;
    }
}
