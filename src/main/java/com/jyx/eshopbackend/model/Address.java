package com.jyx.eshopbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String HouseNumber;

    @Column
    private String buildingNumber;

    @Column
    private String unitNumber;

    @Column(nullable = false)
    private String streetName;

    @Column
    private String community;

    @Column
    private String district;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String province;
    @Column
    private String country;

    @Column(nullable = false)
    private String postalCode;

    @Override
    public String toString() {
        return "Address{" +
                "recipientName='" + recipientName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", HouseNumber='" + HouseNumber + '\'' +
                ", buildingNumber='" + buildingNumber + '\'' +
                ", unitNumber='" + unitNumber + '\'' +
                ", streetName='" + streetName + '\'' +
                ", community='" + community + '\'' +
                ", district='" + district + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
