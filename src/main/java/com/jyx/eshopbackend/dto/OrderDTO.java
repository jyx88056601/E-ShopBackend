package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    private Long id;


    private String orderNumber;


    private double totalAmount;

    private LocalDateTime orderDate;

    private UserResponseDTO userResponseDTO;
    private Shipment shipment;

    private Payment payment;

    private List<OrderItem> orderItems;
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.orderDate = order.getOrderDate();
        this.totalAmount = order.getTotalAmount();
        this.orderItems = order.getOrderItems();
        this.userResponseDTO = new UserResponseDTO(order.getUser());
        this.payment = order.getPayment();
        this.shipment = order.getShipment();
    }

    public Long getId() {
        return id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public UserResponseDTO getUserResponseDTO() {
        return userResponseDTO;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public Payment getPayment() {
        return payment;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
