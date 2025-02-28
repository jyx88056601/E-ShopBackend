package com.jyx.eshopbackend.dto;

public class OrderDTO {

    private final String merchantId;
    private final String customerId;


    public OrderDTO(String merchantId, String customerId) {
        this.merchantId = merchantId;
        this.customerId = customerId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getCustomerId() {
        return customerId;
    }
}
