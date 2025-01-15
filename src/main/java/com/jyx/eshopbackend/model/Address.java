package com.jyx.eshopbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
