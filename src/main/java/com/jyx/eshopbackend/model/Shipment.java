package com.jyx.eshopbackend.model;

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
    private Order order;

    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    private String trackingNumber;
    private LocalDateTime shippedDate;
    private ShipmentStatus status;


    @PrePersist
    protected void onCreate() {
        status = ShipmentStatus.PENDING;
    }

}

