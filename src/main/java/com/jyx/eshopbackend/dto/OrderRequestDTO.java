package com.jyx.eshopbackend.dto;

import java.util.List;

public class OrderRequestDTO{
    private final List<OrderItemRequestDTO> orderItemRequestDTOList;
    public OrderRequestDTO(List<OrderItemRequestDTO> orderItemRequestDTOList) {
        this.orderItemRequestDTOList = orderItemRequestDTOList;
    }

    public List<OrderItemRequestDTO> getOrderItemRequestDTOList() {
        return orderItemRequestDTOList;
    }
}
