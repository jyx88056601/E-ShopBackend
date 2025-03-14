package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.Shipment;
import java.time.format.DateTimeFormatter;

public class ShipmentResponseDTO {
    private String id;
    private String orderId;
    private String addressId;
    private String trackingNumber;
    private String shippedDate;
    private String status;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ShipmentResponseDTO(Shipment shipment) {
        this.id = shipment.getId() != null ? shipment.getId().toString() : "";
        this.orderId = shipment.getOrder() != null && shipment.getOrder().getId() != null ? shipment.getOrder().getId().toString() : "";
        this.addressId = shipment.getAddress() != null && shipment.getAddress().getId() != null ? shipment.getAddress().getId().toString() : "";
        this.trackingNumber = shipment.getTrackingNumber() != null ? shipment.getTrackingNumber() : "";
        this.shippedDate = shipment.getShippedDate() != null ? shipment.getShippedDate().format(FORMATTER) : "";
        this.status = shipment.getStatus() != null ? shipment.getStatus().name() : "";
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getAddressId() {
        return addressId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public String getStatus() {
        return status;
    }
}