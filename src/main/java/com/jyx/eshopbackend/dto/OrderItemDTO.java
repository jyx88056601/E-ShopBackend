package com.jyx.eshopbackend.dto;

public class OrderItemDTO {
   private final String product_id;
   private final String quantity;

    public OrderItemDTO(String product_id, String quantity) {
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getQuantity() {
        return quantity;
    }
}
