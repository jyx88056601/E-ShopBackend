package com.jyx.eshopbackend.dto;

public class OrderDTO {

    private final String merchant_id;
    private final String customer_id;


    public OrderDTO(String merchant_id, String customer_id) {
        this.merchant_id = merchant_id;
        this.customer_id = customer_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }
}
