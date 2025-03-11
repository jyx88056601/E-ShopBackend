package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.Order;

public class OrderDetailDTO {
    private String merchantId;
    private String customerId;
    private String orderNumber;
    private String totalAmount;
    private String orderCreatedTime;
    private String orderItems;
    private String paymentDetail;
    private String shipmentDetail;
    private String orderStatus;

    public OrderDetailDTO(Order order) {
        this.merchantId = String.valueOf(order.getMerchantId());
        this.customerId = String.valueOf(order.getCustomerId());
        this.orderNumber = order.getOrderNumber();
        this.totalAmount = order.getTotalAmount().toString();
        this.orderCreatedTime = order.getOrderCreatedTime().toString();
        this.orderStatus = order.getOrderStatus().name();
    }

    public String getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(String paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public String getShipmentDetail() {
        return shipmentDetail;
    }

    public void setShipmentDetail(String shipmentDetail) {
        this.shipmentDetail = shipmentDetail;
    }

    public String getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(String orderItems) {
        this.orderItems = orderItems;
    }


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderCreatedTime() {
        return orderCreatedTime;
    }

    public void setOrderCreatedTime(String orderCreatedTime) {
        this.orderCreatedTime = orderCreatedTime;
    }


    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}