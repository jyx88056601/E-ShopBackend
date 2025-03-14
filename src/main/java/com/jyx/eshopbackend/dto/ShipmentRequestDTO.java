package com.jyx.eshopbackend.dto;

public class ShipmentRequestDTO {
    public String addressId;
    public String orderId;

    public ShipmentRequestDTO(String addressId, String orderId) {
        this.addressId = addressId;
        this.orderId = orderId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
