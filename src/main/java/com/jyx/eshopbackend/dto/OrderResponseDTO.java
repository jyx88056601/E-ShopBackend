package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.Order;
import com.jyx.eshopbackend.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderResponseDTO extends OrderDTO {


    private final String orderId;

    private final String orderNumber;

    private final String orderStatus;
    private final String createdTime;

    private final List<String> itemDetail;

    private final String totalPrice;


    public OrderResponseDTO(Order order) {
        super(order.getMerchantId().toString(), order.getCustomerId().toString());
        this.totalPrice = String.valueOf(order.getTotalAmount());
        this.orderId = String.valueOf(order.getId());
        this.orderNumber = order.getOrderNumber();
        this.orderStatus = String.valueOf(order.getOrderStatus());
        this.createdTime = String.valueOf(order.getOrderCreatedTime());
        List<String> items = new ArrayList<>();
        for(OrderItem orderItem : order.getOrderItems()) {
            items.add(orderItem.getProductName() + "#" + orderItem.getQuantity() + "#" + orderItem.getTotalPrice());
        }
        itemDetail = new ArrayList<>(items);
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public List<String> getItemDetail() {
        return itemDetail;
    }

    @Override
    public String getCustomerId() {
        return super.getCustomerId();
    }

    @Override
    public String getMerchantId() {
        return super.getMerchantId();
    }
}
