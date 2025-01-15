package com.jyx.eshopbackend.dto;

public class UserDTO {
    private final String username;

    public UserDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
