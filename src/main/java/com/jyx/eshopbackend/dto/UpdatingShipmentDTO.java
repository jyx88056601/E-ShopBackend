package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.Shipment;
import com.jyx.eshopbackend.model.ShipmentStatus;

import java.time.LocalDateTime;

public class UpdatingShipmentDTO {
    private String trackingNumber;
    private LocalDateTime shippedDate;
    private ShipmentStatus status;

    public UpdatingShipmentDTO(Shipment shipment) {
        this.trackingNumber = shipment.getTrackingNumber();
        this.shippedDate =  shipment.getShippedDate();
        this.status = shipment.getStatus();
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public LocalDateTime getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(LocalDateTime shippedDate) {
        this.shippedDate = shippedDate;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }
}
