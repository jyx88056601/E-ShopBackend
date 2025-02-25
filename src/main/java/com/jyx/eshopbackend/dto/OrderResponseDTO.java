package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.Order;

public class OrderResponseDTO extends OrderDTO {
    public OrderResponseDTO(Order order) {
        super(order.getMerchant_id().toString(), order.getCustomer_id().toString());
    }

    @Override
    public String getCustomer_id() {
        return super.getCustomer_id();
    }

    @Override
    public String getMerchant_id() {
        return super.getMerchant_id();
    }
}
