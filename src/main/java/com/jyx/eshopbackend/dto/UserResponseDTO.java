package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.*;

import java.util.List;

public class UserResponseDTO extends UserDTO{
    private String email;
    private String phoneNumber;

    private List<Order> orders;

    private boolean isActive;

    private Cart cart;

    private List<Address> addresses;

    private Role role;

    public String getUsername() {
        return super.getUsername();
    }

    public UserResponseDTO(User user) {
        super(user.getUsername());
        this.isActive = user.isActive();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.addresses = user.getAddresses();
        this.cart = user.getCart();
        this.orders = user.getOrders();
        this.role = user.getRole();
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
