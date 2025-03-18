package com.jyx.eshopbackend.dto;

public class StripeRequestDTO {
    private String orderId;



    public StripeRequestDTO(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
