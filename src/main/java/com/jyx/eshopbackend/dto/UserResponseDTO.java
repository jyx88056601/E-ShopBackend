package com.jyx.eshopbackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jyx.eshopbackend.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class UserResponseDTO extends UserDTO{

    private Long id;
    private String email;
    private String phoneNumber;

    @JsonIgnore
    private List<Order> orders;

    private boolean isActive;



    private Cart cart;

    private LocalDateTime registrationTime;


    private LocalDateTime lastUpdatedDate;
    @JsonIgnore
    private List<Address> addresses;

    private Role role;

    public String getUsername() {
        return super.getUsername();
    }

    public UserResponseDTO(User user) {
        super(user.getUsername());
        this.id = user.getId();
        this.isActive = user.isActive();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.cart = user.getCart();
        this.role = user.getRole();
        this.registrationTime = user.getRegistrationTime();
        this.lastUpdatedDate = user.getLastUpdatedDate();
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Cart getCart() {
        return cart;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return isActive;
    }
}
