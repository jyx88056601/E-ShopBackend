package com.jyx.eshopbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false,  unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime registrationTime;

    @Column(nullable = false)
    private LocalDateTime lastUpdatedDate;

    @Column(nullable = false)
    private boolean isActive;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private Cart cart;


    // store addresses ids in a list for business account
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_address_ids", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "address_id")
    private List<Long> addressIds = new ArrayList<>();

    public List<Long> getAddressIds() {
        return addressIds;
    }

    // store updated products ids in a list for business account
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> productIds = new HashSet<>();

    public Set<Long> getProductIds() {
        return productIds;
    }

    @PrePersist
    private void onCreate() {
        this.registrationTime = LocalDateTime.now();
        this.lastUpdatedDate = LocalDateTime.now();
        this.cart = new Cart(this);
    }

    @PreUpdate
    private void onUpdate() {
        this.lastUpdatedDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setAddressIds(List<Long> addressIds) {
        this.addressIds = addressIds;
    }

    public void setProductIds(Set<Long> productIds) {
        this.productIds = productIds;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

}
