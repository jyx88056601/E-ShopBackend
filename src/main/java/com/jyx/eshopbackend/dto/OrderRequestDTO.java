package com.jyx.eshopbackend.dto;

import java.util.List;

public class OrderRequestDTO extends OrderDTO {

    private final List<OrderItemRequestDTO> orderItemRequestDTOList;
    public OrderRequestDTO(String merchant_id, String customer_id, List<OrderItemRequestDTO> orderItemRequestDTOList) {
        super(merchant_id, customer_id);
        this.orderItemRequestDTOList = orderItemRequestDTOList;
    }

    public List<OrderItemRequestDTO> getOrderItemRequestDTOList() {
        return orderItemRequestDTOList;
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
