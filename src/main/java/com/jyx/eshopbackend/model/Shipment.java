package com.jyx.eshopbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipments", indexes = {@Index(name = "idx_order_id", columnList = "order_id")})
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Order order;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    private String trackingNumber;
    private LocalDateTime shippedDate;
    private ShipmentStatus status;

    public Shipment() {
    }

    public Shipment(Order order) {
        this.order = order;
    }

    @PrePersist
    protected void onCreate() {
        status = ShipmentStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Address getAddress() {
        return address;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public LocalDateTime getShippedDate() {
        return shippedDate;
    }

    public ShipmentStatus getStatus() {
        return status;
    }
}

