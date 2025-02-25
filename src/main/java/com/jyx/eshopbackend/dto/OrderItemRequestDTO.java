package com.jyx.eshopbackend.dto;

public class OrderItemRequestDTO extends OrderItemDTO{
    public OrderItemRequestDTO(String product_id, String quantity) {
        super(product_id, quantity);
    }
}
